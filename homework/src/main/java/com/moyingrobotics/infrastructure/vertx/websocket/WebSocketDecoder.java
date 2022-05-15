package com.moyingrobotics.infrastructure.vertx.websocket;

import com.alibaba.fastjson.JSONObject;

/**
 * 实现类必须是spring bean才能生效
 */
public interface WebSocketDecoder {
    /**
     * 返回此解码器类型，从0开始递增。服务启动后会检查是否有重复，有重复不可启动。
     * @return
     */
    byte getType();
    byte CHAT_CUSTOM_TYPE =0b01000001;
    byte UTF8_TYPE =0b01000010;
    byte FAST_JSON_TYPE = 0b01000011;
    byte PROTO_BUF_TYPE = 0b01000100;

    /**
     * 根据解码类型解码数据。
     * 参数bytes出入的是原始数据，不同解码器需要自己根据实际情况处理/截取掉尾部的元数据。
     * @param bytes
     * @return
     */
    Object getData(byte[] bytes);

    JSONObject getData(String string);
}
