package com.javens;


import com.javens.mq.ProviderService;
import com.javens.mq.RabbitMQConfig;
import com.javens.mq.impl.ProviderServiceImpl;
import com.javens.spring.SpringContext;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RabbitMQProviderApplication {
    protected static final Logger logger = LoggerFactory.getLogger(RabbitMQProviderApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
            produce();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Application context start error");
        }
        synchronized (RabbitMQProviderApplication.class) {
            while (true) {
                try {
                    RabbitMQProviderApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }

    public static void produce() throws Exception{
        RabbitMQConfig config = SpringContext.getBean(RabbitMQConfig.class);
        logger.info(String.format("Host:%s,QUEQUE:%s",config.getHost(),config.getQueueName()));
        ProviderService providerService = SpringContext.getBean(ProviderServiceImpl.class);
        providerService.init();
        providerService.sendMsgSyn("RabbitMQ-1-");
    }

}
