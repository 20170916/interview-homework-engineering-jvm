package com.moyingrobotics.application.product.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class WebSocketDto {
    /**
     * 数据类型
     * 0 登录
     * 1 心跳
     * 2 过期通知
     */
    private byte dataType;
    public static final byte DATA_TYPE_LOGIN =0;
    public static final byte DATA_TYPE_HEART_BEAT =1;
    public static final byte DATA_TYPE_EXPIRATION_NOTIFY =2;
}
