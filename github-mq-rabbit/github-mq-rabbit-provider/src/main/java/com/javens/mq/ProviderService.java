package com.javens.mq;

import java.util.Date;

public interface ProviderService {
    /**
     * 同步发送
     * @param msg
     */
    public void sendMsgSyn(String msg);
    public void createFactory(boolean durable);
    public void close();

}
