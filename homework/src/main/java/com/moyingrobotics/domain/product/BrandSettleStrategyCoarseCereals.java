package com.moyingrobotics.domain.product;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.moyingrobotics.exception.SettleStrategyException;

import java.util.Calendar;
import java.util.Date;

/**
 * 杂粮面包的结算策略
 */
public class BrandSettleStrategyCoarseCereals implements BrandSettleStrategy{
    private Integer brandCategoryId =2;
    /**
     * 杂粮面包结算策略。
     * 过期当天7:00-9:00免费领取。
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
        if(isSameDay==true){
            // 过期当天
            final Calendar calendar = Calendar.getInstance();
            long timeInMillis = calendar.getTimeInMillis();
            // 时间精确到小时
            timeInMillis = timeInMillis - timeInMillis % (3600 * 1000);
            calendar.setTimeInMillis(timeInMillis);
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            final Date currentDate7 = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            final Date currentDate9 = calendar.getTime();
            if(currentDate.after(currentDate7) && currentDate.before(currentDate9)){
                // 过期当天7点-9点，免费赠送
                return 0;
            }else if( currentDate.before(expiration)){
                // 未过期且不在过期当天7点-9点，原价出售
                return price;
            }else{
                // 过期不允许出售
                return -1;
            }

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
