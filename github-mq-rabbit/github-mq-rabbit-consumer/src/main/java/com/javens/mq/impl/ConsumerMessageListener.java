package com.javens.mq.impl;

import com.javens.mq.MessageListener;
import com.javens.mq.RabbitMQConfig;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class ConsumerMessageListener implements MessageListener{
    protected static final Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
    ConnectionFactory factory;
    Channel channel;
    Connection connection;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    public ConsumerMessageListener(){
    }

    public  void init(){
        factory = new ConnectionFactory();
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            boolean durable = true;//是否持久化
            channel.queueDeclare(rabbitMQConfig.getQueueName(), durable, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }

    public void consumer(){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        try {
            channel.basicConsume(rabbitMQConfig.getQueueName(), true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }

    public void close(){
        try {
            if(channel!=null){
                channel.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }

    }


}
