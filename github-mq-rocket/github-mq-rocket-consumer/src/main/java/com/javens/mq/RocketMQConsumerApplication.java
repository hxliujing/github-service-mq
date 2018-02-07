package com.javens.mq;


import com.javens.mq.impl.ProviderServiceImpl;
import com.javens.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RocketMQConsumerApplication {
    protected static final Logger logger = LoggerFactory.getLogger(RocketMQConsumerApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
            RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
            ProviderService providerService = new ProviderServiceImpl(config);
            providerService.createFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Application context start error");
        }
        synchronized (RocketMQConsumerApplication.class) {
            while (true) {
                try {
                    RocketMQConsumerApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }

}
