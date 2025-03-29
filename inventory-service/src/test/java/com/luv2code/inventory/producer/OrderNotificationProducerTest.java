package com.luv2code.inventory.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderNotificationProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private OrderNotificationProducer orderNotificationProducer;

    @BeforeEach
    void setup() {
        orderNotificationProducer = new OrderNotificationProducer(
            rabbitTemplate, "ecommerce.order.topic-exchange",
            "order.failed", "order.confirmed"
        );
    }

    @Test
    void sendFailedOrderNotificationMessage_sendToQueue() {
        UUID uuid = UUID.randomUUID();
        Integer orderId = 1;

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));

        orderNotificationProducer.sendFailedOrderNotificationMessage(uuid, orderId);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("ecommerce.order.topic-exchange"),
                eq("order.failed"),
                Optional.ofNullable(argThat(argument -> argument instanceof OrderNotificationDTO &&
                        ((OrderNotificationDTO) argument).getProductId().equals(uuid.toString()) &&
                        ((OrderNotificationDTO) argument).getOrderId().equals(orderId) &&
                        !((OrderNotificationDTO) argument).isOrderConfirmed() &&
                        ((OrderNotificationDTO) argument).getOrderStatus().equals("FAILED")
                ))
        );
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void sendFailedOrderNotificationMessage_errorHandling() {
        UUID uuid = UUID.randomUUID();
        Integer orderId = 1;

        doThrow(new RuntimeException("Mock exception")).when(rabbitTemplate).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));

        orderNotificationProducer.sendFailedOrderNotificationMessage(uuid, orderId);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void sendConfirmedOrderNotificationMessage_sendToQueue() {
        UUID uuid = UUID.randomUUID();
        Integer orderId = 1;

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));

        orderNotificationProducer.sendConfirmedOrderNotificationMessage(uuid, orderId);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("ecommerce.order.topic-exchange"),
                eq("order.confirmed"),
                Optional.ofNullable(argThat(argument -> argument instanceof OrderNotificationDTO &&
                        ((OrderNotificationDTO) argument).getProductId().equals(uuid.toString()) &&
                        ((OrderNotificationDTO) argument).getOrderId().equals(orderId) &&
                        ((OrderNotificationDTO) argument).isOrderConfirmed() &&
                        ((OrderNotificationDTO) argument).getOrderStatus().equals("CONFIRMED")
                ))
        );
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void sendConfirmedOrderNotificationMessage_errorHandling() {
        UUID uuid = UUID.randomUUID();
        Integer orderId = 1;

        doThrow(new RuntimeException("Mock exception")).when(rabbitTemplate).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));

        orderNotificationProducer.sendConfirmedOrderNotificationMessage(uuid, orderId);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));
        verifyNoMoreInteractions(rabbitTemplate);
    }
}
