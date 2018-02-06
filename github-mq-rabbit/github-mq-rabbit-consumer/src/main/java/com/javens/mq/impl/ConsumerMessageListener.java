package com.javens.mq.impl;

import com.javens.mq.MessageListener;
import com.javens.mq.RabbitMQConfig;
import com.javens.util.RandomUtil;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class ConsumerMessageListener implements MessageListener{
    protected static final Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
    ConnectionFactory factory;
    Channel channel;
    Connection connection;
    String queueName;
    String host;
    public ConsumerMessageListener(String host,String queueName){
        this.host = host;
        this.queueName = queueName;
    }

    public void  createFactory(boolean durable){
        factory = new ConnectionFactory();
        try {
            factory.setHost(host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, durable, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            /**
             * 我们可以使用basicQos方法，并设置prefetchCount=1。这样是告诉RabbitMQ，
             * 再同一时刻，不要发送超过1条消息给一个工作者（worker），直到它已经处理了上一条消息并且作出了响应。
             * 这样，RabbitMQ就会把消息分发给下一个空闲的工作者（worker）。
             */
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);
        } catch (IOException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new  RuntimeException(e);
        }
    }

    public  void createFactory(boolean durable,String exchange){
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
            queueName = channel.queueDeclare().getQueue();//随机队列名
            //绑定
            channel.queueBind(queueName,exchange,"");
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
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try{
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    //Thread.sleep(1000 * RandomUtil.generateRandom(1,5));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw  new RuntimeException(e);
                } /*catch (InterruptedException e) {
                    e.printStackTrace();
                } */finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        try {
            boolean autoAck = false;//消息确认
            channel.basicConsume(queueName, autoAck, consumer);
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
