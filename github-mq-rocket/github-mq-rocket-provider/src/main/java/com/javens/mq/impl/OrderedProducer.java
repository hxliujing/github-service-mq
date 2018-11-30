/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.javens.mq.impl;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.javens.mq.Producer;
import com.javens.mq.RocketMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 顺序消息
 * @author liujing01
 * @version OrderedProducer.java, v 0.1 2018-11-29 23:47
 */
public class OrderedProducer implements Producer {
    protected static final Logger logger = LoggerFactory.getLogger(OrderedProducer.class);
    private RocketMQConfig config;
    private DefaultMQProducer producer;
    public OrderedProducer(RocketMQConfig config){
        this.config = config;
        createFactory();
    }

    /**
     * 初始化
     * @throws MQClientException
     */
    private void createFactory() {
        try{
            producer = new DefaultMQProducer("producer-group-sync");
            producer.setNamesrvAddr(config.getHost());
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
       /* Message message = new Message(config.getTopic(), config.getTag(), "K_"+System.currentTimeMillis(),msg.getBytes());*/

        try {
            Message message = new Message(config.getTopic(), config.getTag(),
                    msg.getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            SendResult sendResult = producer.send(message);
            if(sendResult!=null){
                logger.info("SyncProducer Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),msg);
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void close(){
        if(producer!=null){
            producer.shutdown();
        }
    }
}
