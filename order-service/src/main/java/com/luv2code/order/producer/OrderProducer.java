package com.luv2code.order.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String orderInventoryTopicExchange;
    private final String orderCreatedRoutingKey;

    @Autowired
    public OrderProducer(
            final RabbitTemplate rabbitTemplate,
            @Value("${com.luv2code.rabbitmq.exchange.topic.order}") final String orderTopicExchange,
            @Value("${com.luv2code.rabbitmq.routing-key.order.created}") final String orderCreatedRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderInventoryTopicExchange = orderTopicExchange;
        this.orderCreatedRoutingKey = orderCreatedRoutingKey;
    }

    public void makeOrder(OrderMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    orderInventoryTopicExchange,
                    orderCreatedRoutingKey,
                    message
            );
            log.info("Sent request for making order to inventory service. [productId={}]", message.getProductId());
        } catch (Exception e) {
            log.error("Error sending order message: {}", e.getMessage(), e);
        }
    }
}
