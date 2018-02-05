package com.javens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class AliyunProviderApplication {
    protected static final Logger logger = LoggerFactory.getLogger(AliyunProviderApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
        } catch (Exception e) {
            throw  new RuntimeException("Application context start error");
        }
        synchronized (AliyunProviderApplication.class) {
            while (true) {
                try {
                    AliyunProviderApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }
}
