package com.moyingrobotics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moyingrobotics.application.product.dto.BrandExpiredNotifyDto;
import com.moyingrobotics.application.product.dto.ShopUserLoginDto;
import com.moyingrobotics.application.product.dto.WebSocketDto;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class WebSocketClient extends AbstractVerticle{
    private static  int shopId ;
    public static void main(String[] args) {
        if(args.length==0){
            System.out.println("please give shopId");
            return;
        }

        // 从启动参数中获取shopId
        try{
            shopId = Integer.parseInt(args[0]);
        }catch (NumberFormatException  e){
            System.out.println("shopId param use int");
            return;
        }

        // 启动verx的webSocket的客户端
        Vertx.vertx().deployVerticle(new WebSocketClient());
    }

    @Override
    public void start() throws Exception {
        HttpClient client = vertx.createHttpClient();

        client.webSocket(8080, "localhost", "", websocket -> {
            websocket.result().handler(data -> {
                final byte[] bytes = data.getBytes();
                final JSONObject jsonObjectRet = JSON.parseObject(new String(bytes));
                this.webSocketMessageDispatcher(jsonObjectRet);
                //System.out.println(JSON.parseObject(new String(bytes)).toString());
            });

            final ShopUserLoginDto shopUserLoginDto = this.mockLoginData();
            //websocket.result().writeTextMessage("01-03"+ ":hello from client");
            websocket.result().writeTextMessage(JSON.toJSONString(shopUserLoginDto));
        });


    }

    private void webSocketMessageDispatcher(JSONObject message){
        final Byte dataType = message.getByte("dataType");
        if(dataType.equals(WebSocketDto.DATA_TYPE_EXPIRATION_NOTIFY)){
            // 过期通知
            final BrandExpiredNotifyDto brandExpiredNotifyDto = message.toJavaObject(BrandExpiredNotifyDto.class);

            log.info("收到服务端websocket通知");
            log.info("brand expired notice:" +
                    "\n shopId : {} " +
                    "\n brandId : {} " +
                    "\n expiredBrandName : {} " +
                    "\n expiredBrandCategoryName : {} " +
                    "\n expirationTime : {} \n\n\n",
                brandExpiredNotifyDto.getShopId(),
                brandExpiredNotifyDto.getId(),
                brandExpiredNotifyDto.getName(),
                brandExpiredNotifyDto.getBrandCategoryName(),
                brandExpiredNotifyDto.getExpiration());


        }
    }

    private ShopUserLoginDto mockLoginData(){
        return ShopUserLoginDto.builder()
            .dataType(WebSocketDto.DATA_TYPE_LOGIN)
            .shopId(shopId)
            .deviceId("设备00"+shopId)
            .shopName("店铺00"+shopId)
            .userId(shopId)
            .build();
    }
}
