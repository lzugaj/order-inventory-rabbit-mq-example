package com.luv2code.inventory.config;

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
    private final String orderFailedRoutingKey;
    private final String orderFailedQueue;
    private final String orderConfirmedRoutingKey;
    private final String orderConfirmedQueue;

    public OrderRabbitMQConfig(
            @Value("${com.luv2code.rabbitmq.exchange.topic.order}") String orderTopicExchange,
            @Value("${com.luv2code.rabbitmq.routing-key.order.created}") String orderCreatedRoutingKey,
            @Value("${com.luv2code.rabbitmq.queue.created}") String orderCreatedQueue,
            @Value("${com.luv2code.rabbitmq.routing-key.order.failed}") String orderFailedRoutingKey,
            @Value("${com.luv2code.rabbitmq.queue.failed}") String orderFailedQueue,
            @Value("${com.luv2code.rabbitmq.routing-key.order.confirmed}") String orderConfirmedRoutingKey,
            @Value("${com.luv2code.rabbitmq.queue.confirmed}") String orderConfirmedQueue
    ) {
        this.orderTopicExchange = orderTopicExchange;
        this.orderCreatedRoutingKey = orderCreatedRoutingKey;
        this.orderCreatedQueue = orderCreatedQueue;
        this.orderFailedRoutingKey = orderFailedRoutingKey;
        this.orderFailedQueue = orderFailedQueue;
        this.orderConfirmedRoutingKey = orderConfirmedRoutingKey;
        this.orderConfirmedQueue = orderConfirmedQueue;
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(this.orderCreatedQueue, true);
    }

    @Bean
    public Queue orderFailedQueue() {
        return new Queue(this.orderFailedQueue, true);
    }

    @Bean
    public Queue orderConfirmedQueue() {
        return new Queue(this.orderConfirmedQueue, true);
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

    @Bean
    public Binding bindingOrderFailed() {
        return BindingBuilder
                .bind(orderFailedQueue())
                .to(orderTopicExchange())
                .with(this.orderFailedRoutingKey);
    }

    @Bean
    public Binding bindingOrderConfirmed() {
        return BindingBuilder
                .bind(orderConfirmedQueue())
                .to(orderTopicExchange())
                .with(this.orderConfirmedRoutingKey);
    }
}
