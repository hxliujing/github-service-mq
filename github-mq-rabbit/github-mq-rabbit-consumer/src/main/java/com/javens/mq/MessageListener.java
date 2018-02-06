package com.javens.mq;

public interface MessageListener {
    public void consumer();

    public void init();
    public void close();

}
