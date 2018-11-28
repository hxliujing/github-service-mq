/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.javens.mq.trans;

import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 *
 * @author liujing01
 * @version TransactionCheckListenerImpl.java, v 0.1 2018-11-28 15:23 
 */
public class TransactionCheckListenerImpl implements TransactionCheckListener {
    /**
     * 在这里，我们可以根据由MQ回传的key去数据库查询，这条数据到底是成功了还是失败了。
     * @param msg
     * @return
     */
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        System.out.println("未决事务，服务器回查客户端msg =" + new String(msg.getBody().toString()));
        // return LocalTransactionState.ROLLBACK_MESSAGE;
        return LocalTransactionState.COMMIT_MESSAGE;
        // return LocalTransactionState.UNKNOW;
    }
}
