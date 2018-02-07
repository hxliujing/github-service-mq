package com.javens.mq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RocketMQProviderApplication {
    protected static final Logger logger = LoggerFactory.getLogger(RocketMQProviderApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Application context start error");
        }
        synchronized (RocketMQProviderApplication.class) {
            while (true) {
                try {
                    RocketMQProviderApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }

}
