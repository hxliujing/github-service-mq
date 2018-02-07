package com.javens.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ActiveMQProviderApplication {
    protected static final Logger logger = LoggerFactory.getLogger(ActiveMQProviderApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("ActiveMQProviderApplication start  success----------");
        } catch (Exception e) {
            logger.error("== Application context start error:{}", e.getMessage());
        }

        synchronized (ActiveMQProviderApplication.class) {
            while (true) {
                try {
                    ActiveMQProviderApplication.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }


    }
}
