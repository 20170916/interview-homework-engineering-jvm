package com.moyingrobotics.domain.product;

import java.util.List;
import java.util.Optional;

/**
 * 面包数据仓库
 */
public interface BrandRepository<T, ID> {
    T findById(ID id);
    List<T> findList(int offset, int limit);

    T save(T entity);

}
