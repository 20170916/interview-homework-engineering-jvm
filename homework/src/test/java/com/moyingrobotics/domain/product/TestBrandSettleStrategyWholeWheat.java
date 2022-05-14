package com.moyingrobotics.domain.product;

import com.moyingrobotics.application.product.dto.BrandSettleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestBrandSettleStrategyWholeWheat {

    /**
     * 全麦面包计算策略单元测试
     */
    @Test
    public void testWholeWheatBrandSettleStrategy() {
        final Calendar expirationCalenda = Calendar.getInstance();

        String wholeWheatBrandName = "全麦面包";
        int wholeWheatBrandCategoryId = 1;
        String wholeWheatBrandCategoryName = "全麦面包";
        int wholeWheatBrandPrice = 1200;
        int wholeWheatBrandShelfLife = 48;
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
            brand.setExpiration(expirationCalenda.getTime());
            for (int i = 0; i < 23; i++) {
                expirationCalenda.set(Calendar.HOUR_OF_DAY, i);
                brand.setExpiration(expirationCalenda.getTime());
                final BrandSettleDto brandSettleDto = brandService.settleBrand(list);
                final int priceRet = brandSettleDto.getPriceSum();
                assertEquals(wholeWheatBrandPrice/2, priceRet);
            }

            // 验证未过期，非过期当天原价出售
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.DAY_OF_MONTH, expirationCalenda.get(Calendar.DAY_OF_MONTH)+1);
            brand.setExpiration(expirationCalenda.getTime());
            BrandSettleDto brandSettleDto = brandService.settleBrand(list);
            int priceRet = brandSettleDto.getPriceSum();
            assertEquals(wholeWheatBrandPrice, priceRet);

            // 验证过期非当天，结算价格为0，过期数组有一个元素
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.DAY_OF_MONTH,expirationCalenda.get(Calendar.DAY_OF_MONTH)-1);
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
