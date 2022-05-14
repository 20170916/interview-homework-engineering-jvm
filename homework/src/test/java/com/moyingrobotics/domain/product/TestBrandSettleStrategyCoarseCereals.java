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

@RunWith(PowerMockRunner.class)
//@PowerMockIgnore("javax.management.*")
@PrepareForTest({BrandSettleStrategyCoarseCereals.class})
public class TestBrandSettleStrategyCoarseCereals {



    /**
     * 杂粮面包计算策略单元测试
     */
    @Test
    public void testCoarseCerealsBrandSettleStrategy() {
        final Calendar expirationCalenda = Calendar.getInstance();

        String wholeWheatBrandName = "杂粮面包";
        int wholeWheatBrandCategoryId = 2;
        String wholeWheatBrandCategoryName = "杂粮面包";
        int wholeWheatBrandPrice = 1000;
        int wholeWheatBrandShelfLife = 72;
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
            // mock获取当前时间的逻辑 new Date()，mock当前时间是8点
            final Calendar currentTimeMocked = Calendar.getInstance();
            currentTimeMocked.set(Calendar.HOUR_OF_DAY, 8);
            final Date currentDateMocked = currentTimeMocked.getTime();
            PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(currentDateMocked);

            // 验证过期当天7-9点免费赠送
            brand.setExpiration(expirationCalenda.getTime());
            for (int i = 0; i < 23; i++) {
                expirationCalenda.set(Calendar.HOUR_OF_DAY, i);
                brand.setExpiration(expirationCalenda.getTime());
                final BrandSettleDto brandSettleDto = brandService.settleBrand(list);
                final int priceRet = brandSettleDto.getPriceSum();
                assertEquals(0, priceRet);
            }

            // 验证过期当天未过期，非7-9点，原价出售（12点过期，10点原价售出）
            currentTimeMocked.set(Calendar.HOUR_OF_DAY, 10);
            PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(currentTimeMocked.getTime());
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.HOUR_OF_DAY, 12);
            brand.setExpiration(expirationCalenda.getTime());
            BrandSettleDto brandSettleDto = brandService.settleBrand(list);
            int priceRet = brandSettleDto.getPriceSum();
            assertEquals(wholeWheatBrandPrice, priceRet);

            // 验证过期当天已过期，非7-9点，丢弃（12点过期，13点原价售出）
            currentTimeMocked.set(Calendar.HOUR_OF_DAY, 13);
            PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(currentTimeMocked.getTime());
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.HOUR_OF_DAY, 12);
            brand.setExpiration(expirationCalenda.getTime());
            brandSettleDto = brandService.settleBrand(list);
            priceRet = brandSettleDto.getPriceSum();
            assertEquals(0, priceRet);
            assertEquals(1, brandSettleDto.getExpireDiscardBrandList().size());

            // 验证未过期，非过期当天原价出售
            expirationCalenda.setTime(new Date());
            expirationCalenda.set(Calendar.DAY_OF_MONTH, expirationCalenda.get(Calendar.DAY_OF_MONTH)+1);
            brand.setExpiration(expirationCalenda.getTime());
            brandSettleDto = brandService.settleBrand(list);
            priceRet = brandSettleDto.getPriceSum();
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
