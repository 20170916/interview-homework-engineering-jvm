package com.moyingrobotics.exception;

/**
 * 参数异常。
 */
public class ParamException extends Exception {
    public ParamException(){
        super();
    }

    public ParamException(String msg){
        super(msg);
    }

    /*public static ParamException getParamNullException(){
        return new ParamException("缺少参数异常");
    }*/



}
