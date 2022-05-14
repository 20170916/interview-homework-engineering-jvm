package com.moyingrobotics.domain.product;

import com.moyingrobotics.application.product.dto.BrandSettleDto;
import com.moyingrobotics.exception.ParamException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 面包商品的领域服务
 */
public class BrandService {

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
}
