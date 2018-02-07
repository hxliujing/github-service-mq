package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;

public interface ProviderService {
    public void createFactory() throws MQClientException;
    public void close();

}
