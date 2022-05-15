package com.moyingrobotics.domain.product;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 面包数据仓库内存实现
 */
public class BrandRepositoryCache implements BrandRepository<Brand, Integer>{
    LinkedHashMap<Integer, Brand> brandListCache = new LinkedHashMap<>();

    /**
     * 加载homework演示的测试数据
     * 3个面包店，shopId分别为1 2 3
     *
     */
    public void loadHomeWorkData(){
        String[] brandNameList = {"全麦面包", "杂粮面包", "金枪鱼面包"};
        Integer[] brandCategoryIdList = {1, 2, 3};
        String[] brandCategoryNameList = {"全麦面包", "杂粮面包", "含肉面包"};
        Integer[] brandPriceList = {1200, 1000, 1200};
        Integer[] brandShelfLifeList = {48, 72, 24};
        Integer[] shopIdList = {1, 2, 3};
        // 根据当前时间初始化60个即将过期的面包，每秒钟1个
        final Calendar currentCalendar = Calendar.getInstance();

        for (int i = 0; i < 60; i++) {
            int index =i%3;
            final Brand brand = Brand.builder()
                .id(i)
                .name(brandNameList[index])
                .brandCategoryName(brandCategoryNameList[index])
                .brandCategoryId(brandCategoryIdList[index])
                .price(brandPriceList[index])
                .shelfLife(brandShelfLifeList[index])
                .shopId(shopIdList[index])
                .build();
            currentCalendar.set(Calendar.SECOND, i);
            brand.setExpiration(currentCalendar.getTime());
            brandListCache.put(i, brand);
        }

    }

    @Override
    public Brand findById(Integer id) {
        return brandListCache.get(id);
    }

    @Override
    public List<Brand> findList(int offset, int limit) {
        if(offset>brandListCache.size()-1){
            return null;
        }
        List<Brand> ret = new LinkedList<>();
        int i =0;
        Iterator<Integer> iterator = brandListCache.keySet().iterator();
        while (iterator.hasNext()){
            Integer key = iterator.next();
            if(i >= offset && i < offset+limit){
                ret.add(brandListCache.get(key));
            }
            i++;
            if(i>=offset+limit){
                break;
            }
        }
        return ret;
    }

    @Override
    public Brand save(Brand entity) {
        if(entity==null){
            return null;
        }
        final Integer id = entity.getId();
        if(id==null){
            final Integer nextId = this.getNextId();
            entity.setId(nextId);
        }
        brandListCache.put(entity.getId(), entity);
        return entity;
    }


    /**
     * 获取下一个自增id
     * @return
     */
    public Integer getNextId(){
        final Map.Entry<Integer, Brand> tailEntry = this.getTailByReflection(brandListCache);
        final int newId = tailEntry.getKey() + 1;
        return newId;
    }
    /**
     * 反射获取尾节点id
     * @param map
     * @return
     * @param <K>
     * @param <V>
     */
    private <K, V> Map.Entry<K, V> getTailByReflection(LinkedHashMap<K, V> map){
        try{
            Field tail = map.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            return (Map.Entry<K, V>) tail.get(map);
        }catch (Exception e){
            return null;
        }
    }
}
