package com.javens.mq;

public interface MessageListener {
    public void consumer();

    public void createFactory(boolean durable);
    public  void createFactory(boolean durable,String exchange);
    public void close();

}
