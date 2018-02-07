package com.javens.mq;

public class RocketMQConfig {
    private String ProducerId;
    private String topic;
    private String tag;
    private String host;

    public String getProducerId() {
        return ProducerId;
    }

    public void setProducerId(String producerId) {
        ProducerId = producerId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
