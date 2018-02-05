package com.javens;

import com.javens.common.BaseSpringTestCase;
import com.javens.concurrent.test.ConcurrentTestUtil;
import com.javens.concurrent.test.ResultHandler;
import com.javens.mq.AliyunProviderService;
import com.javens.mq.impl.AliyunProviderServiceImpl;
import com.javens.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class AliyunProviderTest extends BaseSpringTestCase{
    @Autowired
    private AliyunProviderService aliyunProvider;

    @Test
    public void demoSyn() throws ExecutionException, InterruptedException{

        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce:"+ System.currentTimeMillis());
                        aliyunProvider.sendMsgSyn(content);
                        return content;
                    }
                },
                new ResultHandler<String>(){
                    public void handle(String result) {
                        System.out.println(result);
                    }
                },
                1000 * 10000);
        Thread.sleep(1000 * 20);
    }

    @Test
    public void demoAsyn() throws ExecutionException, InterruptedException{
        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce:"+ System.currentTimeMillis());
                        aliyunProvider.sendMsgASyn(content);
                        return content;
                    }
                },
                new ResultHandler<String>(){
                    public void handle(String result) {
                        System.out.println(result);
                    }
                },
                1000 * 100);
        Thread.sleep(1000 * 10 * 1);
    }

    @Test
    public void demoOneway() throws ExecutionException, InterruptedException{
        ConcurrentTestUtil.concurrentTest(100, 50000,
                new Callable<String>() {
                    public String call() throws Exception {
                        String content = String.valueOf("Produce:"+ System.currentTimeMillis());
                        aliyunProvider.sendMsgOneway(content);
                        return content;
                    }
                },
                new ResultHandler<String>(){
                    public void handle(String result) {
                        System.out.println(result);
                    }
                },
                1000 * 100);
        Thread.sleep(1000 * 20);
    }

    @Test
    public void demoTX() throws ExecutionException, InterruptedException{
        String content = String.valueOf("Produce:"+ System.currentTimeMillis());
        aliyunProvider.sendMsgTX(content);
        Thread.sleep(1000 * 10 * 1);
    }

    @Test
    public void demoDelay() throws ExecutionException, InterruptedException{
        String content = String.valueOf("Produce:"+ System.currentTimeMillis());
        aliyunProvider.sendMsgDelay(content,5);
        Thread.sleep(1000 * 20);
    }

    @Test
    public void demoFixedTime() throws ExecutionException, InterruptedException{
        String content = String.valueOf("Produce:"+ System.currentTimeMillis());
        Date date = DateUtil.getMinAfter(new Date(),1);
        aliyunProvider.sendMsgFiexdTime(content,date);
        Thread.sleep(1000 * 60 * 5);
    }
}
