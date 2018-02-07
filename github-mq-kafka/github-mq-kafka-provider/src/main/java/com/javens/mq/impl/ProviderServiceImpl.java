package com.javens.mq.impl;

import com.javens.mq.KafkaMQConfig;
import com.javens.mq.ProviderService;
import com.javens.util.RandomUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProviderServiceImpl implements ProviderService{
    private KafkaProducer<Integer,String> producer;
    private String host;
    private String topic;
    @Override
    public void sendMsgSyn(String content) {
        producer.send(new ProducerRecord<Integer, String>(topic,content));
        System.out.println("Sent:" + content);
    }

    @Override
    public void createFactory(String host,String topic) {
        this.host = host;
        this.topic = topic;
        Properties props=new Properties();
        props.put("bootstrap.servers", host);
        props.put("acks","all");
        props.put("retries",0);
        props.put("batch.size",16384);
        props.put("linger.ms",1);
        props.put("buffer.memory",33554432);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer=new KafkaProducer<Integer,String>(props);

    }

    @Override
    public void close() {
        producer.close();
    }
}
