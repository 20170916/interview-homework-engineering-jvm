package com.moyingrobotics.domain.product;

import com.moyingrobotics.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 面包的对象模型
 */
@Data
@SuperBuilder
public class Brand extends BaseEntity {
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
     * 面包价格，单位精确到分
     */
    private Integer price;

    /**
     * 保质期，时间单位为小时
     */
    private Integer shelfLife;

    /**
     * 过期时间
     */
    private Date expiration;

    /**
     * 面包状态：
     * 0 未过期
     * 1 已过期未下架未通知
     * 2 已过期未下架已通知
     * 3 已过期已下架
     */
    private Byte state =0;
    public static byte BRAND_STATE_UNEXPIRED = 0;
    public static byte BRAND_STATE_EXPIRED_SELLING_UNNOTIFY = 1;
    public static byte BRAND_STATE_EXPIRED_SELLING_NOTIFIED = 2;
    public static byte BRAND_STATE_EXPIRED_DOWNLINE = 3;

    /**
     * 所属店铺id
     */
    private Integer shopId;
}
