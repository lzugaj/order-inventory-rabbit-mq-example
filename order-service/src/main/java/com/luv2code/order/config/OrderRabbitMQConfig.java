package com.luv2code.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderRabbitMQConfig {

    private final String orderTopicExchange;
    private final String orderCreatedRoutingKey;
    private final String orderCreatedQueue;

    public OrderRabbitMQConfig(
            @Value("${com.luv2code.rabbitmq.exchange.topic.order}") String orderTopicExchange,
            @Value("${com.luv2code.rabbitmq.routing-key.order.created}") String orderCreatedRoutingKey,
            @Value("${com.luv2code.rabbitmq.queue.created}") String orderCreatedQueue
    ) {
        this.orderTopicExchange = orderTopicExchange;
        this.orderCreatedRoutingKey = orderCreatedRoutingKey;
        this.orderCreatedQueue = orderCreatedQueue;
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(this.orderCreatedQueue, true);
    }

    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange(this.orderTopicExchange);
    }

    @Bean
    public Binding bindingOrderCreated() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(orderTopicExchange())
                .with(this.orderCreatedRoutingKey);
    }
}
