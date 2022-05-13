package com.moyingrobotics.domain.product;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.moyingrobotics.exception.SettleStrategyException;

import java.util.Date;

/**
 * 全麦面包的结算策略
 */
public class BrandSettleStrategyWholeWheat implements BrandSettleStrategy{
    private Integer brandCategoryId =1;
    /**
     * 全麦面包结算策略。
     * 过期当天半价出售。
     * @param brand
     * @return
     */
    public int settleBrand(Brand brand) throws SettleStrategyException{
        if(!this.brandCategoryId.equals(brand.getBrandCategoryId())){
            throw new SettleStrategyException("面包结算策略不匹配");
        }
        final Date expiration = brand.getExpiration();
        final Date currentDate = new Date();
        final boolean isSameDay = DateUtil.isSameDay(currentDate, expiration);
        final Integer price = brand.getPrice();
        if(isSameDay == true){
            // 过期当天
            return price /2;

        }else if(currentDate.before(expiration)){
            // 未过期
            return price;
        }else{
            // 过期非当天
            return -1;
        }
    }


    public Integer getBrandCategoryId(){
        return this.brandCategoryId;
    }
}
