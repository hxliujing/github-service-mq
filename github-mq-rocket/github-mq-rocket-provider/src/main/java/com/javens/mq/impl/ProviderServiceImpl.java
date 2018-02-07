package com.javens.mq.impl;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.javens.mq.ProviderService;
import com.javens.mq.RocketMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ProviderServiceImpl implements ProviderService {
    protected static final Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
    DefaultMQProducer producer;
    private RocketMQConfig config;
    public ProviderServiceImpl(RocketMQConfig config){
       this.config = config;
    }

    /**
     * 是否持久化
     * @param durable
     *     true: 持久化
     *     false： 非持久化
     */
    public  void createFactory() throws MQClientException {
        producer = new DefaultMQProducer("ProducerGroupName");
        producer.setNamesrvAddr(config.getHost());
        producer.setInstanceName("Producer");
        producer.start();
    }




    /**
     * 发送同步消息
     * @param msg
     */
    public void sendMsgSyn(String msg) {
        String topicId = config.getTopic();
        String tag = config.getTag();
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message message = new Message(topicId, tag, bizKey,msg.getBytes());
        SendResult sendResult = null;
        try {
            sendResult = producer.send(message);
            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),msg);
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" [x] Sent '" + msg + "'");
    }

    public void close(){
        if(producer!=null){
            producer.shutdown();
        }
    }
}
