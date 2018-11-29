package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.javens.concurrent.test.ConcurrentTestUtil;
import com.javens.concurrent.test.ResultHandler;
import com.javens.mq.common.BaseSpringTestCase;
import com.javens.mq.impl.AsyncProducer;
import com.javens.mq.impl.OnewayProducer;
import com.javens.mq.impl.SyncProducer;
import com.javens.spring.SpringContext;
import com.javens.util.RandomUtil;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RocketMQProviderTest extends BaseSpringTestCase {


    /**
     * 发送同步消息
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @Test
    public void demoSync() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final Producer producer = new SyncProducer(config);
        for(int i=0;i<10;i++){
            String content = String.valueOf("Producer-"+ "sync"+"-"+ i);
            producer.send(content);
            Thread.sleep(1000 * 1);
        }
        producer.close();
    }

    /**
     * 发送异步消息
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @Test
    public void demoAsync() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final Producer producer = new AsyncProducer(config);
        for(int i=0;i<10;i++){
            String content = String.valueOf("Producer-"+ "async"+"-"+ i);
            producer.send(content);
            Thread.sleep(1000 * 1);
        }
        producer.close();
    }

    /**
     * 发送单向消息
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @Test
    public void demoOneway() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final Producer producer = new OnewayProducer(config);
        for(int i=0;i<100;i++){
            String content = String.valueOf("Producer-"+ "oneway"+"-"+ i);
            producer.send(content);
            Thread.sleep(1000 * 1);
        }
        producer.close();
    }
}
