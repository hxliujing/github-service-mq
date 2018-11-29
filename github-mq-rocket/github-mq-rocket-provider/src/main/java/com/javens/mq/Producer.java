package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface Producer {
    /**
     * 发送消息
     * @param msg
     */
    public void send(String msg);

    /**
     * 关闭
     */
    public void close();

}
