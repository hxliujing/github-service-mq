package com.javens;


import com.javens.mq.KafkaMQConfig;
import com.javens.mq.ProviderService;
import com.javens.mq.ProviderServiceImpl;
import com.javens.spring.SpringContext;
import com.javens.util.RandomUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Arrays;
import java.util.Properties;

public class KafkaMQConsumerApplication {
    protected static final Logger logger = LoggerFactory.getLogger(KafkaMQConsumerApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
            consumer();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Application context start error");
        }
        synchronized (KafkaMQConsumerApplication.class) {
            while (true) {
                try {
                    KafkaMQConsumerApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }

    private static void consumer() {
        KafkaMQConfig config = SpringContext.getBean(KafkaMQConfig.class);
        ProviderService providerService = new ProviderServiceImpl();
        providerService.createFactory(config.getHost(),config.getTopic());
        providerService.subscrib();
    }


}
