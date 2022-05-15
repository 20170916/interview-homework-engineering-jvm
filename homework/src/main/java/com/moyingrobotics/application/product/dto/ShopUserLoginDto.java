package com.moyingrobotics.application.product.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ShopUserLoginDto extends WebSocketDto{
    /**
     * 店铺id
     */
    private Integer shopId;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 登录设备id
     */
    private String deviceId;
    /**
     * 登录用户id
     */
    private Integer userId;
}
