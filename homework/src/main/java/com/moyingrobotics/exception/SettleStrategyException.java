package com.moyingrobotics.exception;

/**
 * 结算策略相关异常，例如未找到匹配的结算策略异常、结算策略信息不完整异常等。
 */
public class SettleStrategyException extends Exception {
    public SettleStrategyException(){
        super();
    }

    public SettleStrategyException(String msg){
        super(msg);
    }



}
