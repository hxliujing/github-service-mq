package com.javens.mq.impl;

import com.javens.mq.ProviderService;
import com.javens.mq.RabbitMQConfig;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ProviderServiceImpl implements ProviderService {
    protected static final Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
    ConnectionFactory factory;
    Channel channel;
    Connection connection;
    String queueName;
    String host;
    boolean durable;
    public ProviderServiceImpl(String host,String queueName){
        this.queueName = queueName;
        this.host = host;
    }

    /**
     * 是否持久化
     * @param durable
     *     true: 持久化
     *     false： 非持久化
     */
    public  void createFactory(boolean durable){
        this.durable = durable;
        factory = new ConnectionFactory();
        try {
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, durable, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }
    public  void createFactory(boolean durable,String exchange){
        this.durable = durable;
        factory = new ConnectionFactory();
        try {
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            //channel.queueDeclare(queueName, durable, false, false, null);
            /**
             * 直连交换机（direct）,
             * 主题交换机（topic）,
             * （头交换机）headers,
             * 扇型交换机（fanout） 它把消息发送给它所知道的所有队列
             */
            channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            //queueName = channel.queueDeclare().getQueue();//随机队列名
            //绑定
            //channel.queueBind(queueName,exchange,"");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }

    public void sendMsgSyn(String msg,String exchange) {
        try {
            if(durable){
                channel.basicPublish(exchange, "", MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            }else{
                channel.basicPublish(exchange, "", null , msg.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
        System.out.println(" [x] Sent '" + msg + "'");
    }


    /**
     * 发送同步消息
     * @param msg
     */
    public void sendMsgSyn(String msg) {
            try {
                if(durable){
                    channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
                }else{
                    channel.basicPublish("", queueName, null , msg.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw  new RuntimeException(e);
            }
            System.out.println(" [x] Sent '" + msg + "'");
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
