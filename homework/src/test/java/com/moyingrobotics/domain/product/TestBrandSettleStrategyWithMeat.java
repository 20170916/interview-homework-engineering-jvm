package com.moyingrobotics.domain.product;

import com.moyingrobotics.application.product.dto.BrandSettleDto;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestBrandSettleStrategyWithMeat {

    /**
     * 含肉类型面包计算策略单元测试
     */
    @Test
    public void testWithMeatBrandSettleStrategy() {
        final Calendar expirationCalenda = Calendar.getInstance();

        String wholeWheatBrandName = "金枪鱼面包";
        int wholeWheatBrandCategoryId = 3;
        String wholeWheatBrandCategoryName = "含肉类型面包";
        int wholeWheatBrandPrice = 1200;
        int wholeWheatBrandShelfLife = 24;
        final Brand brand = Brand.builder()
                .id(1)
                .name(wholeWheatBrandName)
                .brandCategoryName(wholeWheatBrandCategoryName)
                .brandCategoryId(wholeWheatBrandCategoryId)
                .price(wholeWheatBrandPrice)
                .shelfLife(wholeWheatBrandShelfLife)
                //.expiration(expirationCalenda.getTime())
                .build();
        final BrandService brandService = new BrandService();
        List<Brand> list = new LinkedList<Brand>();
        list.add(brand);
        try {
            // 验证过期当天半价出售
           /* brand.setExpiration(expirationCalenda.getTime());
            for (int i = 0; i < 23; i++) {
                expirationCalenda.set(Calendar.HOUR_OF_DAY, i);
                brand.setExpiration(expirationCalenda.getTime());
                final BrandSettleDto brandSettleDto = brandService.settleBrand(list);
                final int priceRet = brandSettleDto.getPriceSum();
                assertEquals(wholeWheatBrandPrice/2, priceRet);
            }*/

            // 验证未过期，原价出售
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.HOUR_OF_DAY, expirationCalenda.get(Calendar.HOUR_OF_DAY)+1);
            brand.setExpiration(expirationCalenda.getTime());
            BrandSettleDto brandSettleDto = brandService.settleBrand(list);
            int priceRet = brandSettleDto.getPriceSum();
            assertEquals(wholeWheatBrandPrice, priceRet);

            // 验证过期非当天，结算价格为0，过期数组有一个元素
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.HOUR_OF_DAY,expirationCalenda.get(Calendar.HOUR_OF_DAY)-1);
            brand.setExpiration(expirationCalenda.getTime());
            brandSettleDto = brandService.settleBrand(list);
            priceRet = brandSettleDto.getPriceSum();
            assertEquals(0, priceRet);
            assertEquals(1, brandSettleDto.getExpireDiscardBrandList().size());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }




}
