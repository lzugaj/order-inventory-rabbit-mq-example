package com.luv2code.inventory.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.luv2code.inventory.producer.OrderStatus.*;

@Slf4j
@Component
public class OrderNotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String orderInventoryTopicExchange;
    private final String orderFailedRoutingKey;
    private final String orderConfirmedRoutingKey;

    @Autowired
    public OrderNotificationProducer(
            final RabbitTemplate rabbitTemplate,
            @Value("${com.luv2code.rabbitmq.exchange.topic.order}") final String orderTopicExchange,
            @Value("${com.luv2code.rabbitmq.routing-key.order.failed}") final String orderFailedRoutingKey,
            @Value("${com.luv2code.rabbitmq.routing-key.order.confirmed}") final String orderConfirmedRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderInventoryTopicExchange = orderTopicExchange;
        this.orderFailedRoutingKey = orderFailedRoutingKey;
        this.orderConfirmedRoutingKey = orderConfirmedRoutingKey;
    }

    public void sendFailedOrderNotificationMessage(UUID productId, Integer orderId) {
        try {
            rabbitTemplate.convertAndSend(
                    orderInventoryTopicExchange,
                    orderFailedRoutingKey,
                    failedMessage(productId, orderId)
            );
            log.info("Ordering process for product failed. [productId={}]", productId);
        } catch (Exception e) {
            log.error("Error sending order failed notification: {}", e.getMessage(), e);
        }
    }

    private OrderNotificationDTO failedMessage(UUID productId, Integer orderId) {
        return OrderNotificationDTO.builder()
                .orderConfirmed(false)
                .orderStatus(FAILED.name())
                .productId(String.valueOf(productId))
                .orderId(orderId)
                .build();
    }

    public void sendConfirmedOrderNotificationMessage(UUID productId, Integer orderId) {
        try {
            rabbitTemplate.convertAndSend(
                    orderInventoryTopicExchange,
                    orderConfirmedRoutingKey,
                    confirmedMessage(productId, orderId)
            );
            log.info("Ordering process for product confirmed. [productId={}]", productId);
        } catch (Exception e) {
            log.error("Error sending order confirmed notification: {}", e.getMessage(), e);
        }
    }

    private OrderNotificationDTO confirmedMessage(UUID productId, Integer orderId) {
        return OrderNotificationDTO.builder()
                .orderConfirmed(true)
                .orderStatus(CONFIRMED.name())
                .productId(String.valueOf(productId))
                .orderId(orderId)
                .build();
    }
}
