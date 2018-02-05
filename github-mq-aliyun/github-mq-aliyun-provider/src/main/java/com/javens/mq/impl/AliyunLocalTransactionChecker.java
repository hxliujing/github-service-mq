package com.javens.mq.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.javens.mq.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class AliyunLocalTransactionChecker implements LocalTransactionChecker {
    protected static final Logger logger = LoggerFactory.getLogger(AliyunLocalTransactionChecker.class);
    @Autowired
    private BusinessService businessService;
    public TransactionStatus check(Message msg) {
        logger.info("开始回查本地事务状态");
        TransactionStatus status = TransactionStatus.Unknow;
        if(businessService.check()){
            //本地事务已成功、提交消息
            status =  TransactionStatus.RollbackTransaction;
        }else{
            //本地事务已失败、回滚消息
            status =  TransactionStatus.RollbackTransaction;
        }
        logger.warn("Message Id:{}transactionStatus:{}Context:{}", msg.getMsgID(), status.name(), new String(msg.getBody()));
        return status;
    }


}
