package com.moyingrobotics.infrastructure.vertx.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

public class WebSocketDecoderFastJson implements WebSocketDecoder {
    private static final byte METADATA_LENGTH=3;
    @Override
    public byte getType() {
        return WebSocketDecoder.FAST_JSON_TYPE;
    }

    @Override
    public Object getData(byte[] bytes) {
        if(bytes==null || bytes.length<=2){
            return null;
        }
        try {
            final String websocketString = new String(bytes, 0, bytes.length - METADATA_LENGTH, "utf-8");
            //log.info(" \n-- {}",JSONObject.parseObject(udpString));
            //return JSONObject.parseObject(udpString).getInnerMap();
            return JSONObject.parseObject(websocketString);
        } catch (UnsupportedEncodingException e) {
            //log.error("websocketString decode fastJson request error.{}",bytes);
            return null;
        }
    }

    //@Override
    public  <T> T getDate(byte[] bytes, Class<T> clazz) {
        if(bytes==null || bytes.length<=2){
            return null;
        }
        try {
            final String udpString = new String(bytes, 0, bytes.length - METADATA_LENGTH, "utf-8");
            //log.info(" \n-- {}",JSONObject.parseObject(udpString, clazz));
            return JSONObject.parseObject(udpString, clazz);
        } catch (UnsupportedEncodingException e) {
            //log.error("websocketString decode fastJson request error.{}",bytes);
            return null;
        }
    }

    @Override
    public JSONObject  getData(String string) {
        return JSONObject.parseObject(string);
    }
}
