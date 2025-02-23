package com.luv2code.order.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String orderInventoryTopicExchange;
    private final String orderCreatedRoutingKey;

    @Autowired
    public OrderCreateProducer(
            final RabbitTemplate rabbitTemplate,
            @Value("${com.luv2code.rabbitmq.exchange.topic.order}") final String orderTopicExchange,
            @Value("${com.luv2code.rabbitmq.routing-key.order.created}") final String orderCreatedRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderInventoryTopicExchange = orderTopicExchange;
        this.orderCreatedRoutingKey = orderCreatedRoutingKey;
    }

    public void makeOrder(OrderCreatedMessage message) {
        rabbitTemplate.convertAndSend(
                orderInventoryTopicExchange,
                orderCreatedRoutingKey,
                message
        );
    }
}
