package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface ProviderService {
    /**
     * 同步发送
     * @param msg
     */
    public void sendMsgSyn(String msg);
    public void createFactory() throws MQClientException;
    public void close();

}
