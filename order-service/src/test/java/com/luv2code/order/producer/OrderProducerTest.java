package com.luv2code.order.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private OrderProducer orderProducer;

    private final String exchange = "ecommerce.order.topic-exchange";
    private final String routingKey = "order.created";

    @BeforeEach
    void setup() {
        orderProducer = new OrderProducer(
                rabbitTemplate,
                exchange,
                routingKey
        );
    }

    @Test
    void makeOrder_sendsMessageToCorrectExchangeAndRoutingKey() {
        OrderMessage message = OrderMessage.builder()
                .orderId(1L)
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();

        orderProducer.makeOrder(message);

        verify(rabbitTemplate).convertAndSend(exchange, routingKey, message);
    }

    @Test
    void makeOrder_logsErrorIfRabbitThrowsException() {
        OrderMessage message = OrderMessage.builder()
                .orderId(1L)
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();

        doThrow(new AmqpException("fail")).when(rabbitTemplate)
                .convertAndSend(exchange, routingKey, message);

        orderProducer.makeOrder(message);

        verify(rabbitTemplate).convertAndSend(exchange, routingKey, message);
    }
}