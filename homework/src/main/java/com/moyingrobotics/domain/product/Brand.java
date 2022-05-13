package com.moyingrobotics.domain.product;

import com.moyingrobotics.domain.BaseEntity;
import lombok.Data;
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
}
