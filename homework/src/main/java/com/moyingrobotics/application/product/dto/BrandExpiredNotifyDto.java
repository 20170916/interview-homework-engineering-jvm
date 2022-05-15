package com.moyingrobotics.application.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 面包过期通知dto
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BrandExpiredNotifyDto extends WebSocketDto{
    /**
     * 面包id
     */
    private Integer id;
    /**
     * 面包名称。
     */
    private String name;

    /**
     * 面包品类名称，目前支持3种：全麦、杂粮、含肉。
     */
    private String brandCategoryName;

    /**
     * 面包品类id，目前支持3种：全麦面包id为1、杂粮面包id为2、含肉面包id为3。
     */
    private Integer brandCategoryId;

    /**
     * 保质期，时间单位为小时
     */
    private Integer shelfLife;

    /**
     * 过期时间
     */
    private Date expiration;

    /**
     * 所属店铺id
     */
    private Integer shopId;

}
