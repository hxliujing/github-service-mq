package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface ProviderService {
    /**
     * 同步发送
     * @param msg
     */
    public void sendMsgSyn(String msg);

    /**
     * 发送顺序消息
     * @param msg
     */
    public void sendMsgSequnce(String msg);

    /**
     * 发送事务消息
     * @param msg
     */
    public void sendMessageInTransaction(String msg) throws MQClientException;

    /**
     * 初始化
     * @throws MQClientException
     */
    public void createFactory() throws MQClientException;

    /**
     * 关闭
     */
    public void close();

}
