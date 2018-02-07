package com.javens.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;

public class ProviderServiceImpl implements ProviderService{
    private KafkaConsumer<String, String> kafkaConsumer;
    private String host;
    private String topic;

    @Override
    public void createFactory(String host,String topic) {
        this.host = host;
        this.topic = topic;
        Properties props = new Properties();
        props.put("bootstrap.servers", host);
        props.put("group.id", "group-1");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<String,String>(props);
    }

    public void subscrib(){
        kafkaConsumer.subscribe(Arrays.asList(topic));
        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }
    }

    @Override
    public void close() {
        kafkaConsumer.close();
    }
}
