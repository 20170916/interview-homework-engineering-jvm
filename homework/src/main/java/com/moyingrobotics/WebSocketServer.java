package com.moyingrobotics;

import cn.hutool.core.lang.Singleton;
import com.moyingrobotics.domain.product.BrandRepositoryCache;
import com.moyingrobotics.domain.product.BrandService;
import com.moyingrobotics.infrastructure.vertx.websocket.WebSocketVerticle;
import io.vertx.core.Vertx;

public class WebSocketServer {
    public static void main(String[] args) {
        // 初始化面包店数据
        final BrandRepositoryCache brandRepositoryCache = Singleton.get(BrandRepositoryCache.class);
        brandRepositoryCache.loadHomeWorkData();


        // 开启vertx web socket服务
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(WebSocketVerticle.class.getName());

        // 开启面包点过期通知
        final BrandService brandService = Singleton.get(BrandService.class);
        brandService.notifyExpiration();
    }
}
