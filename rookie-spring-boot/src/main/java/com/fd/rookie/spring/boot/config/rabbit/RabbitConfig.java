package com.fd.rookie.spring.boot.config.rabbit;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RabbitConfig {
    public static final String queueName = "queue";     //表示队列名称
    public static final String queueKey = "topic.*";    //表示的是路由规则

    @Bean
    Queue queueA() {
        //第二个参数表示是否需要持久化
        return new Queue(queueName, true);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("spring-boot-exchange");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("FanoutExchange");
    }

    @Bean
    Binding binding(Queue queueA, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueA).to(topicExchange).with(queueKey);
    }

    /**
     * 创建一个消息监听容器，然后把消费者注册到该容器中
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        //如果有消息来，那么调用消费者的指定方法
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    /**
     * MessageListenerAdapter 可以看做是 我们消费者的一个包装类
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
