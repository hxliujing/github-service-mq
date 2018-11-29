package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface Consumer {
    /**
     * 关闭
     */
    public void close();

}
