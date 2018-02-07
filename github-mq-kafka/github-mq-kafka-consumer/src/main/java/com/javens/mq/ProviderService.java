package com.javens.mq;

public interface ProviderService {
    public void createFactory(String host, String topic);
    public void subscrib();
    public void close();

}
