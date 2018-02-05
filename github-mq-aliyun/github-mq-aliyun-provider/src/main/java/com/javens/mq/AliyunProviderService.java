package com.javens.mq;

import java.util.Date;

public interface AliyunProviderService {
    /**
     * 同步发送
     * 原理：同步发送是指消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式。
     * 应用场景：此种方式应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等。
     * @param msg
     */
    public void sendMsgSyn(String msg);

    /**
     * 异步发送
     * 原理：异步发送是指发送方发出数据后，不等接收方发回响应，接着发送下个数据包的通讯方式。MQ 的异步发送，需要用户实现异步发送回调接口（SendCallback），在执行消息的异步发送时，应用不需要等待服务器响应即可直接返回，通过回调接口接收服务器响应，并对服务器的响应结果进行处理。
     * 应用场景：异步发送一般用于链路耗时较长，对 RT 响应时间较为敏感的业务场景，例如用户视频上传后通知启动转码服务，转码完成后通知推送转码结果等。
     * @param msg
     */
    public void sendMsgASyn(String msg);

    /**
     * 单向发送
     * 原理：单向（Oneway）发送特点为只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
     * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     * @param msg
     */
    public void sendMsgOneway(String msg);

    /**
     * 事务消息
     * @param msg
     */
    public void sendMsgTX(String msg);

    /**
     * 延时消息
     * @param msg
     * @param seconds
     */
    public void sendMsgDelay(String msg,int seconds);

    public void sendMsgFiexdTime(String msg,Date date);
}
