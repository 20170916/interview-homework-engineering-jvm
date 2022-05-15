package com.moyingrobotics.infrastructure.vertx.websocket;

import com.alibaba.fastjson.JSONObject;
import com.moyingrobotics.application.product.dto.WebSocketDto;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketVerticle extends AbstractVerticle {

    /**
     * 保存每一个连接到服务器的通道
     * key: userId-shopId
     * value: webSocket
     */

    private static final String CHAT_CHANNEL = "chat";

    private WebSocketDecoder webSocketDecoder= new WebSocketDecoderFastJson();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        //this.notifyExpiration();

        vertx.createHttpServer().webSocketHandler(handler -> {
            System.out.println("client connected: "+handler.textHandlerID());

            vertx.eventBus().consumer(CHAT_CHANNEL, message -> {
                handler.writeTextMessage((String)message.body());
            });

            handler.textMessageHandler(message -> {
                //System.out.println(message);
                System.out.println(handler);
                vertx.eventBus().publish(CHAT_CHANNEL,message);

                final JSONObject jsonObject = webSocketDecoder.getData(message);
                //System.out.println(jsonObject.toJSONString());
                this.dispatcher(jsonObject, handler);
            });
            /*handler.frameHandler(frame->{
                final byte[] bytes = frame.binaryData().getBytes();
                //final Object data = webSocketDecoder.getData(bytes);
            });*/

            handler.closeHandler(message ->{
                System.out.println("client disconnected "+handler.textHandlerID());
            });

        }).listen(8080);
    }


    private void dispatcher(JSONObject jsonObject, ServerWebSocket handler){
        final Byte dataType = jsonObject.getByte("dataType");
        if(dataType== WebSocketDto.DATA_TYPE_LOGIN){
            // 登录类型数据
            log.info("新的webSocket客户端上线 " +
                "\n店铺名称：{}"+
                " 店铺id：{} \n",
                jsonObject.getString("shopName"),
                jsonObject.getInteger("shopId"));
            WebSocketVerticleContext.addClient(jsonObject.getInteger("shopId"), handler);
        }
    }

}
