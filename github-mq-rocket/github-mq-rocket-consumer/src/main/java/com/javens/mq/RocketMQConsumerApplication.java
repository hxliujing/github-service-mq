package com.javens.mq;


import com.javens.mq.impl.ConsumerService;
import com.javens.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

public class RocketMQConsumerApplication {
    protected static final Logger logger = LoggerFactory.getLogger(RocketMQConsumerApplication.class);


    public void start(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run(){
                System.out.println("Execute Hook.....");
            }
        }));
    }

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
            RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
            new ConsumerService(config);

            //shutdown hook
            new RocketMQConsumerApplication().start();


        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Application context start error");
        }
    }

}
