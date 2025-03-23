package com.luv2code.order.consumer;

import com.luv2code.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderNotificationConsumerTest {

    @Mock
    private OrderNotificationMapper orderNotificationMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderNotificationConsumer orderNotificationConsumer;

    @Test
    void failedOrderNotification_processOrder() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", false);
        orderNotification.put("orderStatus", "FAILED");
        orderNotification.put("productId", String.valueOf(UUID.randomUUID()));
        orderNotification.put("orderId", 1);

        OrderNotificationDTO orderNotificationDTO = OrderNotificationDTO.builder()
                .orderConfirmed(false)
                .orderStatus("FAILED")
                .productId((String) orderNotification.get("productId"))
                .orderId(1)
                .build();

        when(orderNotificationMapper.toDTO(orderNotification))
                .thenReturn(orderNotificationDTO);

        orderNotificationConsumer.failedOrderNotification(orderNotification);

        verify(orderNotificationMapper, times(1)).toDTO(any());
        verify(orderService, times(1)).processOrder(any());
        verifyNoMoreInteractions(orderNotificationMapper, orderService);
    }

    @Test
    void confirmedOrderNotification_processOrder() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", true);
        orderNotification.put("orderStatus", "CONFIRMED");
        orderNotification.put("productId", String.valueOf(UUID.randomUUID()));
        orderNotification.put("orderId", 1);

        OrderNotificationDTO orderNotificationDTO = OrderNotificationDTO.builder()
                .orderConfirmed(true)
                .orderStatus("CONFIRMED")
                .productId((String) orderNotification.get("productId"))
                .orderId(1)
                .build();

        when(orderNotificationMapper.toDTO(orderNotification))
                .thenReturn(orderNotificationDTO);

        orderNotificationConsumer.failedOrderNotification(orderNotification);

        verify(orderNotificationMapper, times(1)).toDTO(any());
        verify(orderService, times(1)).processOrder(any());
        verifyNoMoreInteractions(orderNotificationMapper, orderService);
    }
}
