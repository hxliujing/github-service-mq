package com.javens.mq.impl;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.javens.mq.AliyunProviderService;
import com.javens.mq.BusinessService;
import com.javens.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AliyunProviderServiceImpl implements AliyunProviderService {
    protected static final Logger logger = LoggerFactory.getLogger(AliyunProviderServiceImpl.class);

    @Autowired
    private Producer producer;
    @Autowired
    private TransactionProducer transactionProducer;
    @Autowired
    private BusinessService businessService;

    public void sendMsgSyn(String context) {
        String topicId = ((ProducerBean)producer).getProperties().getProperty("TopicId");
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message msg = new Message(topicId, "1234", bizKey,context.getBytes());
        try{
            SendResult sendResult = producer.send(msg);
            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),context);
            }
        } catch (ONSClientException e){
            logger.info("发送失败!");
        }

    }

    public void sendMsgASyn(String context) {
        String topicId = ((ProducerBean)producer).getProperties().getProperty("TopicId");
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message msg = new Message(topicId, "1234", bizKey,context.getBytes());
        try{
            producer.sendAsync(msg, new SendCallback() {
                public void onSuccess(SendResult sendResult) {
                    // 消费发送成功
                    logger.info("send message success. topic=" + sendResult.getTopic() + ", msgId=" + sendResult.getMessageId());
                }
                public void onException(OnExceptionContext context) {
                    // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                    logger.error(LogUtils.getStackTrace(context.getException()));
                    logger.info("send message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
                }
            });
        } catch (ONSClientException e){
            logger.info("发送失败!");
        }
        // 在 callback 返回之前即可取得 msgId。
        logger.info("send message async. topic=" + msg.getTopic() + ", msgId=" + msg.getMsgID());
    }

    public void sendMsgOneway(String context) {
        String topicId = ((ProducerBean)producer).getProperties().getProperty("TopicId");
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message msg = new Message(topicId, "1234", bizKey,context.getBytes());
        try{
            producer.sendOneway(msg);
        } catch (ONSClientException e){
            logger.info("发送失败!");
        }
    }

    public void sendMsgTX(String context) {
        String topicId = ((TransactionProducerBean)transactionProducer).getProperties().getProperty("TopicId");
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message msg = new Message(topicId, "1234", bizKey,context.getBytes());
        try{
            SendResult sendResult = transactionProducer.send(msg,new LocalTransactionExecuter(){
                public TransactionStatus execute(Message msg, Object arg) {
                    logger.info("执行本地事务");
                    TransactionStatus status = TransactionStatus.Unknow;
                   /* boolean isCommit = false;
                    isCommit  = businessService.execute();
                    if(isCommit){
                        // 本地事务成功，提交事务
                        status =  TransactionStatus.CommitTransaction;
                    }else {
                        //本地事务失败，回滚事务
                        status =  TransactionStatus.RollbackTransaction;
                    }*/
                    logger.warn("Message Id:{}transactionStatus:{}", msg.getMsgID(), status.name());
                    return status;
                }
            },null);

            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),context);
            }
        } catch (ONSClientException e){
            logger.info("发送失败!");
        }
    }

    public void sendMsgDelay(String context,int seconds) {
        String topicId = ((ProducerBean)producer).getProperties().getProperty("TopicId");
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message msg = new Message(topicId, "1234", bizKey,context.getBytes());
        msg.setStartDeliverTime(System.currentTimeMillis() + seconds * 1000);
        try{
            SendResult sendResult = producer.send(msg);
            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),context);
            }
        } catch (ONSClientException e){
            logger.info("发送失败!");
        }
    }

    public void sendMsgFiexdTime(String context, Date date) {
        String topicId = ((ProducerBean)producer).getProperties().getProperty("TopicId");
        String bizKey = "RZ-"+System.currentTimeMillis();
        Message msg = new Message(topicId, "1234", bizKey,context.getBytes());
        msg.setStartDeliverTime(date.getTime());
        try{
            SendResult sendResult = producer.send(msg);
            if(sendResult!=null){
                logger.info("Send Success: sendResult={},context= {}", JSON.toJSONString(sendResult),context);
            }
        } catch (ONSClientException e){
            logger.info("发送失败!");
        }
    }
}
