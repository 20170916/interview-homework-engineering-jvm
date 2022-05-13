package com.moyingrobotics.domain.product;

import com.moyingrobotics.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 面包种类的对象模型
 */
@Data
@SuperBuilder
public class BrandCategory extends BaseEntity {
    /**
     *  面包品类名称，目前支持3种：全麦、杂粮、含肉。
     */
    private String brandCategoryName;

}
