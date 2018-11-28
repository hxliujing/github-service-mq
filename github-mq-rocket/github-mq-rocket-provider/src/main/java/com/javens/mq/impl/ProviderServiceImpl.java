package com.javens.mq.impl;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.javens.mq.ProviderService;
import com.javens.mq.RocketMQConfig;
import com.javens.mq.trans.TransactionCheckListenerImpl;
import com.javens.mq.trans.TransactionExecuterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ProviderServiceImpl implements ProviderService {
    protected static final Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);
    DefaultMQProducer producer;
    private RocketMQConfig config;
    public ProviderServiceImpl(RocketMQConfig config){
       this.config = config;
    }

    /**
     * 是否持久化
     * @param durable
     *     true: 持久化
     *     false： 非持久化
     */
    public  void createFactory() throws MQClientException {
        producer = new DefaultMQProducer("producer-group-name");
        producer.setNamesrvAddr(config.getHost());
       // producer.setInstanceName("producer");
        producer.start();
    }


    /**
     * 发送同步消息
     * @param msg
     */
    public void sendMsgSyn(String msg) {
        Message message = new Message(config.getTopic(), config.getTag(), "K_"+System.currentTimeMillis(),msg.getBytes());
        try {
            SendResult sendResult = producer.send(message);
            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),msg);
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送顺序消息
     * @param msg
     */
    public void sendMsgSequnce(String msg){
        Message message = new Message(config.getTopic(), config.getTag(), "K_"+System.currentTimeMillis(),msg.getBytes());
        try {
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, 0);
            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),msg);
            }

        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送事务消息
     * @param msg
     */
    public void sendMessageInTransaction(String msg) throws MQClientException {
        // 未决事务，MQ服务器回查客户端
        // 也就是上文所说的，当RocketMQ发现`Prepared消息`时，会根据这个Listener实现的策略来决断事务
        TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
        // 构造事务消息的生产者
        TransactionMQProducer producer = new TransactionMQProducer("producer-group-name");
        // 设置事务决断处理类
        producer.setTransactionCheckListener(transactionCheckListener);
        // 本地事务的处理逻辑，相当于示例中检查Bob账户并扣钱的逻辑
        TransactionExecuterImpl tranExecuter = new TransactionExecuterImpl();

        producer.start();
        // 构造MSG，省略构造参数
        Message message = new Message(config.getTopic(), config.getTag(), "K_"+System.currentTimeMillis(),msg.getBytes());
        // 发送消息
        SendResult sendResult = producer.sendMessageInTransaction(message, tranExecuter, null);
        if(sendResult!=null){
            logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),msg);
        }
    }

    public void close(){
        if(producer!=null){
            producer.shutdown();
        }
    }
}
