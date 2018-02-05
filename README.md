## 消息队列
    github-mq-active   activeMQ
        github-mq-active-consumer   #消费者
        github-mq-active-provider   #生产者
    github-mq-aliyun   aliyunMQ
        github-mq-aliyun-consumer   #消费者
        github-mq-aliyun-provider   #生产者
    github-mq-kafka    kafkaMQ
         github-mq-kafka-consumer   #消费者
         github-mq-kafka-provider   #生产者
    github-mq-rabbit   rabbitMQ
        github-mq-rabbit-consumer   #消费者
        github-mq-rabbit-provider   #生产者
    github-mq-rocket   rocketMQ
        github-mq-rocket-consumer   #消费者
        github-mq-rocket-provider   #生产者
##  压测环境
    处理器：2.9 GHz Intel Core i7
    内存： 16 GB 1600 MHz DDR3
    压测前置条件： threads:100,times: 50000
    网络： 阿里云MQ外网 流量5MB网络 
## github-mq-aliyun-provider 
|   发送方式 | 发送TPS | 发送反馈结果 | 可靠性 | TPS | 备注 |
| :-----:   | :-----:   |  :-----: |  :-----:   | :-----:   | :-----:   |
| 同步发送 | 快 | 有 | 不丢失 | 344 |  |
| 异步发送 | 快 | 有 | 不丢失 |  3984   | 外网流量压爆 |
| 单向发送 | 最快 | 无 | 可能丢失 |   4398  | 外网流量压爆 |
 
    
     
  