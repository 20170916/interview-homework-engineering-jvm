package com.moyingrobotics.application.product.dto;

import com.moyingrobotics.domain.product.Brand;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 面包商品结算模型
 */
@Data
@Builder
public class BrandSettleDto {
    /**
     * 结算商品清单，面包->结算价格
     */
    private Map<Brand,Integer> settleMap;

    /**
     * 总价
     */
    private int priceSum;

    /**
     * 未过期面包
     */
    private List<Brand> freshBrandList;

    /**
     * 过期面包
     */
    private List<Brand> expireBrandList;

    /**
     * 过期折扣面包
     */
    private List<Brand> expireDiscountBrandList;
    
    /**
     * 过期需要立即销毁的面包
     */
    private List<Brand> expireDiscardBrandList;



}
