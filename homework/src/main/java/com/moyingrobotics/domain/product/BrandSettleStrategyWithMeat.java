package com.moyingrobotics.domain.product;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.moyingrobotics.exception.SettleStrategyException;

import java.util.Calendar;
import java.util.Date;

/**
 * 含肉类型面包的结算策略
 */
public class BrandSettleStrategyWithMeat implements BrandSettleStrategy{
    private Integer brandCategoryId =3;
    /**
     * 全麦面包结算策略。
     * 过期后立即销毁。
     * @param brand
     * @return
     */
    public int settleBrand(Brand brand) throws SettleStrategyException{
        if(!this.brandCategoryId.equals(brand.getBrandCategoryId())){
            throw new SettleStrategyException("面包结算策略不匹配");
        }
        final Date expiration = brand.getExpiration();
        final Date currentDate = new Date();
        final long between = DateUtil.between(currentDate, expiration, DateUnit.DAY);
        final Integer price = brand.getPrice();
        if(currentDate.before(expiration)){
            // 未过期
            return price;
        }else{
            // 过期
            return -1;
        }
    }

    public Integer getBrandCategoryId(){
        return this.brandCategoryId;
    }
}
