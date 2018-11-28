package com.javens.mq;

public class RocketMQConfig {
    private String producerId;
    private String consumerId;
    private String topic;
    private String tag;
    private String host;


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

    /**
     * Getter method for property <tt>producerId</tt>.
     *
     * @return property value of producerId
     */
    public String getProducerId() {
        return producerId;
    }

    /**
     * Setter method for property <tt>producerId</tt>.
     *
     * @param producerId value to be assigned to property producerId
     */
    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    /**
     * Getter method for property <tt>consumerId</tt>.
     *
     * @return property value of consumerId
     */
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * Setter method for property <tt>consumerId</tt>.
     *
     * @param consumerId value to be assigned to property consumerId
     */
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
