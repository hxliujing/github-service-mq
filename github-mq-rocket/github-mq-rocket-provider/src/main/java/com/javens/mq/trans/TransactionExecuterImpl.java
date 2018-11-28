/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.javens.mq.trans;

import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;

/**
 *执行本地事务
 * @author liujing01
 * @version TransactionExecuterImpl.java, v 0.1 2018-11-28 15:29 
 */
public class TransactionExecuterImpl implements LocalTransactionExecuter {
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        System.out.println("执行本地事务msg = " + new String(msg.getBody()));
        System.out.println("执行本地事务arg = " + arg);
        String tags = msg.getTags();
        if (tags.equals("transaction2")) {
            System.out.println("======我的操作============，失败了  -进行ROLLBACK");
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
        // return LocalTransactionState.UNKNOW;
    }
}
