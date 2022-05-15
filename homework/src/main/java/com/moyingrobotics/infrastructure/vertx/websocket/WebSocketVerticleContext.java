package com.moyingrobotics.infrastructure.vertx.websocket;

import cn.hutool.core.lang.Singleton;
import com.alibaba.fastjson.JSONObject;
import com.moyingrobotics.application.product.dto.ShopUserLoginDto;
import com.moyingrobotics.application.product.dto.WebSocketDto;
import com.moyingrobotics.domain.product.BrandService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.ServerWebSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketVerticleContext{

    /**
     * 保存每一个连接到服务器的通道
     * key: shopId
     * value: webSocket
     */
    private static Map<Integer, ServerWebSocket> connectionMap = new HashMap<>(16);


    private WebSocketDecoder webSocketDecoder= new WebSocketDecoderFastJson();


    /**
     * 添加websocket客户端
     * @param shopId
     * @param handler
     */
    public static void addClient(Integer shopId, ServerWebSocket handler){
        connectionMap.put(shopId, handler);
    }

    /**
     * 获取websocket客户端
     */
    public static ServerWebSocket getServerWebSocket(Integer shopId){
        return connectionMap.get(shopId);
    }





}
