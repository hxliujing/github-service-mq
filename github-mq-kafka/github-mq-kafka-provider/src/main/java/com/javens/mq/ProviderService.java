package com.javens.mq;

public interface ProviderService {
    /**
     * 同步发送
     * @param msg
     */
    public void sendMsgSyn(String msg);
    public void createFactory(String host,String topic);
    public void close();

}
