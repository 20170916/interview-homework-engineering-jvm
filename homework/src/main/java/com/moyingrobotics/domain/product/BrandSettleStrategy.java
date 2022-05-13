package com.moyingrobotics.domain.product;

import com.moyingrobotics.domain.product.Brand;
import com.moyingrobotics.exception.SettleStrategyException;

/**
 * 面包店内面包的结算策略，使用策略模式实现。
 */
public interface BrandSettleStrategy {
    /**
     * 面包商品结算策略。
     *
     * 返回值>0,表意商品的价格。
     * 返回值=0,表意商品刚过期不就，赠送客户的。
     * 返回值=-1,表意商品以过期，应该丢弃。
     * @param brand
     * @return
     */
    public int settleBrand(Brand brand) throws SettleStrategyException;

    /**
     * 面包的类别id。
     * @return
     */
    public Integer getBrandCategoryId();
}
