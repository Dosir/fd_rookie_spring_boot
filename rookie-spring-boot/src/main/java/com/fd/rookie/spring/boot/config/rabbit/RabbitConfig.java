package com.fd.rookie.spring.boot.config.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 Queue:消息的载体,每个消息都会被投到一个或多个队列。
 Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 Producer:消息生产者,就是投递消息的程序.
 Consumer:消息消费者,就是接受消息的程序.
 Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
@Configuration
public class RabbitConfig {
    private final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";

    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey.A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey.#";

    //dead letter exchange
    public static final String EXCHANGE_DELAY = "my-mq-exchange_DELAY";

    //延迟队列名称
    public static final String QUEUE_DELAY = "QUEUE_DELAY";

    //延迟队列的routing-key
    public static final String ROUTING_KEY_DELAY = "";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange: 通过添加属性key-value匹配
     DirectExchange: 按照routingkey分发到指定队列
     TopicExchange: 多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_B);
    }

    @Bean
    public Queue queueA() {
        //第二个参数表示是否需要持久化
        return new Queue(QUEUE_A, true);
    }

    @Bean
    public Queue queueB() {
        //第二个参数表示是否需要持久化
        return new Queue(QUEUE_B, true);
    }

    @Bean
    public Queue delayProcessQueue() {
        Map<String, Object> params = new HashMap<>();
        //x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称
        params.put("x-dead-letter-exchange", EXCHANGE_A);
        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称
        params.put("x-dead-letter-routing-key", ROUTINGKEY_A);
        return new Queue(QUEUE_DELAY, true, false, false, params);
    }

    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(EXCHANGE_DELAY);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(delayProcessQueue()).to(delayExchange()).with(ROUTING_KEY_DELAY);
    }

    @Bean
    Binding binding(Queue queueA, DirectExchange defaultExchange) {
        return BindingBuilder.bind(queueA).to(defaultExchange).with(ROUTINGKEY_A);
    }

    @Bean
    public Binding bindingOne(){
        return BindingBuilder.bind(queueA()).to(topicExchange()).with(ROUTINGKEY_A);
    }

    @Bean
    public Binding bindingTwo(){
        return BindingBuilder.bind(queueB()).to(topicExchange()).with(ROUTINGKEY_B);
    }
//
//    /**
//     * 创建一个消息监听容器，然后把消费者注册到该容器中
//     * @param connectionFactory
//     * @param listenerAdapter
//     * @return
//     */
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        //如果有消息来，那么调用消费者的指定方法
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    Receiver receiver() {
//        return new Receiver();
//    }
//
//    /**
//     * MessageListenerAdapter 可以看做是 我们消费者的一个包装类
//     * @param receiver
//     * @return
//     */
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
}
