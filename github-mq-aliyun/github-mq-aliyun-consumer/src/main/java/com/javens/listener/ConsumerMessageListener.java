package com.javens.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerMessageListener implements MessageListener {
    protected static final Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);


    public Action consume(Message message, ConsumeContext consumeContext) {
        System.out.println("Receive: " +  new String(message.getBody()));
        try {
            /*logger.info(">>>>>日志mq开始消费<<<<<" + "messageId=" + message.getMsgID());
            Thread.sleep(1000 * 1);
            logger.info(">>>>>日志mq结束消费<<<<<" + "messageId=" + message.getMsgID());*/
            // 确认消息
            return Action.CommitMessage;
        } catch (Exception e) {
            // 消费失败，通知再发
            e.printStackTrace();
            return Action.ReconsumeLater;
        }
    }
}
