package com.javens.mq.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.javens.mq.Consumer;
import com.javens.mq.RocketMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ConsumerService implements Consumer {
    protected static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private DefaultMQPushConsumer consumer;
    private RocketMQConfig config;
    public ConsumerService(RocketMQConfig config){
       this.config = config;
       createFactory();
    }

   public  void createFactory() {
        try{
            consumer = new DefaultMQPushConsumer("consumer-group-name");
            consumer.setNamesrvAddr(config.getHost());
            consumer.subscribe(config.getTopic(),config.getTag());
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for(MessageExt msg: msgs){
                        logger.info("Receive: " + msg.getMsgId()+"," + new String(msg.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

   }


    public void close(){
        if(consumer!=null){
            consumer.shutdown();
        }
    }
}
