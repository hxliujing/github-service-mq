package com.javens.mq;

import java.util.Date;

public interface ProviderService {
    /**
     * 同步发送
     * @param msg
     */
    public void sendMsgSyn(String msg);
    public void createFactory(boolean durable);
    //交换机模式
    public  void createFactory(boolean durable,String exchange);
    public void sendMsgSyn(String msg,String exchange);
    public void close();

}
