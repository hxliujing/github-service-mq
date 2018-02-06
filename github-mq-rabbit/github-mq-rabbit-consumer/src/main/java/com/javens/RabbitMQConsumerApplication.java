package com.javens;


import com.javens.mq.MessageListener;
import com.javens.mq.RabbitMQConfig;
import com.javens.mq.impl.ConsumerMessageListener;
import com.javens.spring.SpringContext;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.IOException;

public class RabbitMQConsumerApplication {
    protected static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumerApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
            comsumerNonPersistent();
            comsumerPersistent();
        } catch (Exception e) {
            throw  new RuntimeException("Application context start error");
        }
        synchronized (RabbitMQConsumerApplication.class) {
            while (true) {
                try {
                    RabbitMQConsumerApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }

    /**
     * 非持久化消息
     * @throws Exception
     */
    public static void comsumerNonPersistent() throws Exception{
        RabbitMQConfig config = SpringContext.getBean(RabbitMQConfig.class);
        logger.info(String.format("Host:%s,QUEQUE:%s",config.getHost(),config.getQueueName()));
        MessageListener messageListener = new ConsumerMessageListener(config.getHost(),config.getQueueName());
        messageListener.createFactory(false);
        messageListener.consumer();
    }

    /**
     * 持久化消息
     * @throws Exception
     */
    public static void comsumerPersistent() throws Exception{
        RabbitMQConfig config = SpringContext.getBean(RabbitMQConfig.class);
        logger.info(String.format("Host:%s,QUEQUE:%s",config.getHost(),"hello-2"));
        MessageListener messageListener = new ConsumerMessageListener(config.getHost(),"hello-2");
        messageListener.createFactory(true);
        messageListener.consumer();
    }
}
