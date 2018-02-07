package com.javens.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ActiveMQConsumerApplication {
    protected static final Logger logger = LoggerFactory.getLogger(ActiveMQConsumerApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("ActiveMQConsumerApplication start  success----------");
        } catch (Exception e) {
            logger.error("== Application context start error:{}", e.getMessage());
        }

        synchronized (ActiveMQConsumerApplication.class) {
            while (true) {
                try {
                    ActiveMQConsumerApplication.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }


    }
}
