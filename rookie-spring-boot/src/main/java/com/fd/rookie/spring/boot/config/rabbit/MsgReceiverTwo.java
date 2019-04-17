package com.fd.rookie.spring.boot.config.rabbit;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消费者
 */
@Component
public class MsgReceiverTwo {
    private final Logger logger = LoggerFactory.getLogger(MsgReceiverTwo.class);

    //@RabbitListener注解可以注册在类上，也可以注册在方法上
    @RabbitListener(queues = RabbitConfig.QUEUE_B)
    @RabbitHandler
    public void process(String content, Message message, Channel channel) {
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            logger.info("接收处理队列B当中的消息： " + content);
            // TODO 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                // TODO 处理失败,重新压入MQ
                channel.basicRecover();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
