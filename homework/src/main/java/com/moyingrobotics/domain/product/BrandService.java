package com.moyingrobotics.domain.product;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Singleton;
import com.alibaba.fastjson.JSONObject;
import com.moyingrobotics.application.product.dto.BrandExpiredNotifyDto;
import com.moyingrobotics.application.product.dto.BrandSettleDto;
import com.moyingrobotics.application.product.dto.ShopUserLoginDto;
import com.moyingrobotics.application.product.dto.WebSocketDto;
import com.moyingrobotics.exception.ParamException;
import com.moyingrobotics.infrastructure.vertx.websocket.WebSocketVerticleContext;
import io.vertx.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 面包商品的领域服务
 */
@Slf4j
public class BrandService {
    private BrandRepository brandRepository = Singleton.get(BrandRepositoryCache.class);

    /**
     * 根据传入的面包列表，进行结算。<p>
     *
     * 注意事项：
     * 1 参数中面包的价格与最终的返回的结算价格，单位精确到分。
     * @param brandList
     * @return
     */
    public BrandSettleDto settleBrand(List<Brand> brandList) throws Exception{
        // 参数校验
        if(brandList==null){
            throw new ParamException("参数缺失 brandList");
        }

        int priceSum =0;
        final BrandSettleDto ret = BrandSettleDto.builder()
            .settleMap(new LinkedHashMap<>())
            .freshBrandList(new LinkedList<>())
            .expireBrandList(new LinkedList<>())
            .expireDiscountBrandList(new LinkedList<>())
            .expireDiscardBrandList(new LinkedList<>())
            .build();

        // 遍历面包，进行结算
        for (Brand brand : brandList) {
            if(brand==null){
                throw new ParamException("参数缺失 brandList");
            }
            final BrandSettleStrategyContext settleStrategyContext =
                BrandSettleStrategyContext.getInstance();
            final int price = settleStrategyContext.settleBrand(brand);

            if(price==-1){
                // 过期丢弃
                ret.getExpireDiscardBrandList().add(brand);
            }else if(price != brand.getPrice()){
                // 过期折扣
                ret.getExpireDiscountBrandList().add(brand);
                ret.getSettleMap().put(brand, price);
                priceSum += price;
            }else{
                // 原价出售
                ret.getFreshBrandList().add(brand);
                ret.getSettleMap().put(brand, price);
                priceSum += price;
            }
        }

        ret.setPriceSum(priceSum);
        return ret;
    }

    public void notifyExpiration(){
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
               try{
                   this.doNotifyExpiration();
               }catch (Throwable t){
                   log.error("error notifyExpiration, {}",t);
               }

            },
            0, 3, TimeUnit.SECONDS);
    }

    private void doNotifyExpiration(){
        // 分批次遍历所有未下架面包，对未下架的过期面包，使用socket提醒客户端过期，做下架处理
        int offset =0;
        int limit = 50;
        List<Brand> brandList = brandRepository.findList(offset, limit);
        while (brandList!=null && brandList.size()>0){
            for (int i = 0; i < brandList.size(); i++) {
                final Brand brand = brandList.get(i);

                //for (Brand brand : brandList) {
                // 获取面包状态
                final Byte latestState = this.getLatestState(brand);
                // 对过期未下架的面包推送socket通知
                if(latestState.equals(Brand.BRAND_STATE_EXPIRED_SELLING_UNNOTIFY)){
                    final Integer shopId = brand.getShopId();
                    final ServerWebSocket serverWebSocket =
                        WebSocketVerticleContext.getServerWebSocket(shopId);
                    if(serverWebSocket==null){
                        // 客户端未登录，不通知
                        continue;
                    }
                    final BrandExpiredNotifyDto brandExpiredNotifyDto =
                        BeanUtil.toBean(brand, BrandExpiredNotifyDto.class);
                    brandExpiredNotifyDto.setDataType(WebSocketDto.DATA_TYPE_EXPIRATION_NOTIFY);
                    serverWebSocket.writeTextMessage(JSONObject.toJSONString(brandExpiredNotifyDto));
                    // 通知成功后，更新状态为已过期未下架已通知
                    brand.setState(Brand.BRAND_STATE_EXPIRED_SELLING_NOTIFIED);
                }
            }
            offset+=limit;
            brandList =brandRepository.findList(offset, limit);
        }
    }


    /**
     * 更新面包状态
     * @param brand
     */
    public void updateBrandState(Brand brand){
        if(brand==null){
            return;
        }
        Byte state = brand.getState();
        if(state==null){
            state=Brand.BRAND_STATE_UNEXPIRED;
            brand.setState(Brand.BRAND_STATE_UNEXPIRED);
        }

        // 检查是否过期
        if(state.equals(Brand.BRAND_STATE_UNEXPIRED)){
            final Date expiration = brand.getExpiration();
            final Date currentDate = new Date();
            if(currentDate.after(expiration) ){
                brand.setState(Brand.BRAND_STATE_EXPIRED_SELLING_UNNOTIFY);
            }
        }
    }

    /**
     * 获取最新的状态
     * @param brand
     * @return
     */
    public Byte getLatestState(Brand brand){
        this.updateBrandState(brand);
        return brand.getState();
    }

}
