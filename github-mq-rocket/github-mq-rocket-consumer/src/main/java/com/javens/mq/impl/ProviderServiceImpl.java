package com.javens.mq.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.javens.mq.ProviderService;
import com.javens.mq.RocketMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class ProviderServiceImpl implements ProviderService {
    protected static final Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
    DefaultMQPushConsumer consumer;
    private RocketMQConfig config;
    public ProviderServiceImpl(RocketMQConfig config){
       this.config = config;
    }

   public  void createFactory() throws MQClientException {
        consumer = new DefaultMQPushConsumer("ProducerGroupName");
        consumer.setNamesrvAddr(config.getHost());
        consumer.setInstanceName("Consumber");
        consumer.subscribe(config.getTopic(),config.getTag());
        consumer.setConsumeMessageBatchMaxSize(50);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            /**
             * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
             */
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for(MessageExt msg : msgs){
                    System.out.println(msg.getTags()+"|Receive: " +  new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

       change();
    }

    private void change() {
        /*new Thread(new Runnable() {
            public void run() {
               while (true){
                   try {
                       Thread.sleep(1000 * 10);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   if(consumer!=null){
                       try {
                           consumer.subscribe(config.getTopic(),"TAG2");
                       } catch (MQClientException e) {
                           e.printStackTrace();
                       }
                   }
               }
            }
        }).start();*/
    }


    public void close(){
        if(consumer!=null){
            consumer.shutdown();
        }
    }
}
