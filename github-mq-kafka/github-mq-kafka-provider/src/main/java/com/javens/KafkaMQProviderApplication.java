package com.javens;


import com.javens.util.RandomUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Properties;

public class KafkaMQProviderApplication {
    protected static final Logger logger = LoggerFactory.getLogger(KafkaMQProviderApplication.class);

    public static void main(String[] args) {
        try {
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            ctx.load("spring-base.xml");
            ctx.refresh();
            logger.info("Application start  success----------");
            provider();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Application context start error");
        }
        synchronized (KafkaMQProviderApplication.class) {
            while (true) {
                try {
                    KafkaMQProviderApplication.class.wait();
                } catch (InterruptedException e) {
                    throw  new RuntimeException("synchronized error");
                }
            }
        }
    }

    private static void provider() {
        Properties props=new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks","all");
        props.put("retries",0);
        props.put("batch.size",16384);
        props.put("linger.ms",1);
        props.put("buffer.memory",33554432);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<Integer,String> producer=new KafkaProducer<Integer,String>(props);
        String topic = "RZ-TP-KAFKA";
        for(int i=0;i<100;i++){
            String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
            producer.send(new ProducerRecord<Integer, String>(topic,content));
            System.out.println("Sent:" + content);
        }

    }

}
