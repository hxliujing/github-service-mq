package com.javens.mq;

import com.javens.concurrent.test.ConcurrentTestUtil;
import com.javens.concurrent.test.ResultHandler;
import com.javens.mq.common.BaseSpringTestCase;
import com.javens.mq.impl.ProviderServiceImpl;
import com.javens.util.RandomUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class KafkaMQProviderTest extends BaseSpringTestCase {
    @Autowired
    private KafkaMQConfig config;

    /**
     * 同步非持久化消息
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoSyn() throws ExecutionException, InterruptedException{
        final ProviderService providerService = new ProviderServiceImpl();
        providerService.createFactory(config.getHost(),config.getTopic());
        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce-"+ System.currentTimeMillis()+"-"+ RandomUtil.generateRandom(1,10000));
                        providerService.sendMsgSyn("KafkaMQ-1-" + content);
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
