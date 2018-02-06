package com.javens.mq;

import java.util.Date;

public interface ProviderService {
    /**
     * 同步发送
     * @param msg
     */
    public void sendMsgSyn(String msg);

    public void init();
    public void close();

}
