package com.javens.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.javens.concurrent.test.ConcurrentTestUtil;
import com.javens.concurrent.test.ResultHandler;
import com.javens.mq.common.BaseSpringTestCase;
import com.javens.mq.impl.ProviderServiceImpl;
import com.javens.spring.SpringContext;
import com.javens.util.RandomUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RabbitMQProviderTest extends BaseSpringTestCase {


    @Test
    public void demoSequence() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final ProviderService providerService = new ProviderServiceImpl(config);
        providerService.createFactory();
        for(int i=0;i<999;i++){
            String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ i);
            providerService.sendMsgSequnce("RocketMQ-" + content);
            Thread.sleep(1000 * 1);
        }

    }


    @Test
    public void demoSyn4() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final ProviderService providerService = new ProviderServiceImpl(config);
        providerService.createFactory();
        for(int i=0;i<999;i++){
            String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ i);
            providerService.sendMsgSyn("RocketMQ-1-" + content);
            Thread.sleep(1000 * 2);
        }

    }


    @Test
    public void demoSyn() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final ProviderService providerService = new ProviderServiceImpl(config);
        providerService.createFactory();
        ConcurrentTestUtil.concurrentTest(10, 100,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
                        providerService.sendMsgSyn("RocketMQ-1-" + content);
                        return content;
                    }
                },
                new ResultHandler<String>(){
                    public void handle(String result) {
                        System.out.println(result);
                    }
                },
                1000 * 10000);
        Thread.sleep(1000 * 10);
    }


    @Test
    public void demoSyn2() throws ExecutionException, InterruptedException, MQClientException {
        RocketMQConfig config = SpringContext.getBean(RocketMQConfig.class);
        final ProviderService providerService = new ProviderServiceImpl(config);
        providerService.createFactory();
        ConcurrentTestUtil.concurrentTest(10, 20,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
                        providerService.sendMsgSyn("RocketMQ-1-" + content);
                        return content;
                    }
                },
                new ResultHandler<String>(){
                    public void handle(String result) {
                        System.out.println(result);
                    }
                },
                1000 * 10000);
        Thread.sleep(1000 * 10);
    }
}
