package com.javens.mq.impl;

import com.javens.mq.ProviderService;
import com.javens.mq.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Component
public class ProviderServiceImpl implements ProviderService {
    protected static final Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    ConnectionFactory factory;
    Channel channel;
    Connection connection;

    public ProviderServiceImpl(){
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

    /**
     * 发送同步消息
     * @param msg
     */
    public void sendMsgSyn(String msg) {
        for(int i=0;i<100;i++){
            String message = msg+ i ;
            try {
                channel.basicPublish("", rabbitMQConfig.getQueueName(), MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                throw  new RuntimeException(e);
            }
            System.out.println(" [x] Sent '" + message + "'");
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
