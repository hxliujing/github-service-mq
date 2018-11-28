package com.javens.mq.impl;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
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
        consumer = new DefaultMQPushConsumer("consumer-group-name");
        consumer.setNamesrvAddr(config.getHost());
        //consumer.setInstanceName("consumer");
       /**
        * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        * 如果非第一次启动，那么按照上次消费的位置继续消费
        */
       consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
       consumer.subscribe(config.getTopic(),config.getTag());
       consumer.setConsumeMessageBatchMaxSize(50);
       /**
        * 实现了MessageListenerOrderly表示一个队列只会被一个线程取到
        *，第二个线程无法访问这个队列
        */
       consumer.registerMessageListener(new MessageListenerOrderly() {
           public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
               // 设置自动提交
               context.setAutoCommit(true);
               for(MessageExt msg : msgs){
                   System.out.println(config.getTopic()+"|" + msg.getTags()+"|Receive: " +  new String(msg.getBody()));
               }
               return ConsumeOrderlyStatus.SUCCESS;
           } });
       /*consumer.registerMessageListener(new MessageListenerConcurrently() {
           public ConsumeConcurrentlyStatus consumeMessage(
                   List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
               for(MessageExt msg : msgs){
                    System.out.println(msg.getTags()+"|Receive: " +  new String(msg.getBody()));
               }
               return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
       }});*/
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
