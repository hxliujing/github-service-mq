package com.javens.mq;

import com.javens.concurrent.test.ConcurrentTestUtil;
import com.javens.concurrent.test.ResultHandler;
import com.javens.mq.common.BaseSpringTestCase;
import com.javens.mq.impl.ProviderServiceImpl;
import com.javens.spring.SpringContext;
import com.javens.util.DateUtil;
import com.javens.util.RandomUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RabbitMQProviderTest extends BaseSpringTestCase {
    @Autowired
    private RabbitMQConfig config;

    /**
     * 同步非持久化消息
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoSynNonPersistent() throws ExecutionException, InterruptedException{
        final ProviderService providerService = new ProviderServiceImpl(config.getHost(),config.getQueueName());
        providerService.createFactory(false);
        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
                        providerService.sendMsgSyn("RabbitMQ-1-" + content);
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

    /**
     * 同步持久化消息
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoSynPersistent() throws ExecutionException, InterruptedException{
        final ProviderService providerService = new ProviderServiceImpl(config.getHost(),"hello-2");
        providerService.createFactory(true);
        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
                        providerService.sendMsgSyn("RabbitMQ-1-" + content);
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

    /**
     * 交换机
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoSynExchange() throws ExecutionException, InterruptedException{
        final ProviderService providerService = new ProviderServiceImpl(config.getHost(),"hello-4");
        providerService.createFactory(false,"rz-exchange");
        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
                        providerService.sendMsgSyn("RabbitMQ-1-" + content,"rz-exchange");
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
