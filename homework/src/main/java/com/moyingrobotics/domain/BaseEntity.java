package com.moyingrobotics.domain;

import java.io.Serializable;

import cn.hutool.core.util.ObjectUtil;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * JavaBean领域对象的共同基类，定义了ID属性和其访问字段
 *
 **/
@Data
@SuperBuilder
public class BaseEntity implements Serializable {

    private Integer id;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        BaseEntity baseEntity = (BaseEntity) obj;

        return ObjectUtil.equals(baseEntity.getId(), this.id);
    }

}

