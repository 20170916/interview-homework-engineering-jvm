package com.moyingrobotics.domain.product;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.moyingrobotics.exception.ParamException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *  面包店内面包的结算策略的上下文类。
 */
public class BrandSettleStrategyContext {
    /**
     * 用来缓存面包店面包的结算策略。
     * 当前结算策略在当前类的初始化时进行结算策略初始化，后面若结算策略比较多是，
     * 可以通过从配置文件中加载结算策略或从远端分布式配置中心加载结算策略。
     * key->value表意面包类别id->面包结算策略
     */
    private Map<Integer, BrandSettleStrategy> brandSettleStrategyMapCache = new HashMap<>();
    {
        loadDefaultSettleStrategy();
        loadSettleStrategyFromLocalFile();
        loadSettleStrategyFromRemoteConfig();
    }

    /**
     * 使用反射加载项目中默认提供的结算策略
     *
     */
    private void loadDefaultSettleStrategy(){
        Class superClass = BrandSettleStrategy.class;
        Set<Class<?>> classes = ClassUtil.scanPackage("com.moyingrobotics.domain.product");
        Set<Class<?>> collect = classes.stream().filter(sonClass -> {
            boolean allAssignableFrom = ClassUtil.isAllAssignableFrom(new Class[]{superClass},
                    new Class[]{sonClass});
            //要将 本身排除
            return allAssignableFrom && sonClass != superClass;
        }).collect(Collectors.toSet());

        for (Class<?> aClass : collect) {
            final BrandSettleStrategy brandSettleStrategy = (BrandSettleStrategy) ReflectUtil.newInstance(aClass);
            final Integer brandCategoryId = brandSettleStrategy.getBrandCategoryId();
            brandSettleStrategyMapCache.put(brandCategoryId, brandSettleStrategy);
        }

    }

    /**
     * 从本地加载结算策略
     */
    private void loadSettleStrategyFromLocalFile(){
        // TODO: 2022/5/12
    }

    /**
     * 从远端配置加载结算策略
     */
    private void loadSettleStrategyFromRemoteConfig(){
        // TODO: 2022/5/12
    }

    private BrandSettleStrategyContext(){}
    private static volatile BrandSettleStrategyContext instance;

    /**
     * BrandSettleStrategyContext的单例对象
     * @return
     */
    public static BrandSettleStrategyContext getInstance(){
        if(instance==null){
            synchronized (BrandSettleStrategyContext.class){
                if(instance==null){
                    instance = new BrandSettleStrategyContext();
                }
            }
        }
        return instance;
    }

    public int settleBrand(Brand brand) throws Exception{
        final Integer brandCategoryId = brand.getBrandCategoryId();
        if(brandCategoryId==null){
            throw new ParamException("缺少参数异常");
        }
        final BrandSettleStrategy brandSettleStrategy = brandSettleStrategyMapCache.get(brandCategoryId);
        return brandSettleStrategy.settleBrand(brand);
    }
}
