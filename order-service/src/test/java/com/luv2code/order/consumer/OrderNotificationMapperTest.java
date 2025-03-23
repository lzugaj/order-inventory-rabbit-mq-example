package com.luv2code.order.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderNotificationMapperTest {

    @InjectMocks
    private OrderNotificationMapper orderNotificationMapper;

    @Test
    void toDTO_orderConfirmedIsNull_throwsIllegalArgumentException() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", null);
        orderNotification.put("orderStatus", "FAILED");
        orderNotification.put("productId", String.valueOf(UUID.randomUUID()));
        orderNotification.put("orderId", 1);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderNotificationMapper.toDTO(orderNotification)
        );

        String expectedMessage = "Required fields are missing in the order notification message.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toDTO_orderStatusIsNull_throwsIllegalArgumentException() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", false);
        orderNotification.put("orderStatus", null);
        orderNotification.put("productId", String.valueOf(UUID.randomUUID()));
        orderNotification.put("orderId", 1);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderNotificationMapper.toDTO(orderNotification)
        );

        String expectedMessage = "Required fields are missing in the order notification message.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toDTO_productIdIsNull_throwsIllegalArgumentException() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", false);
        orderNotification.put("orderStatus", "FAILED");
        orderNotification.put("productId", null);
        orderNotification.put("orderId", 1);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderNotificationMapper.toDTO(orderNotification)
        );

        String expectedMessage = "Required fields are missing in the order notification message.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toDTO_orderIdIsNull_throwsIllegalArgumentException() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", false);
        orderNotification.put("orderStatus", "FAILED");
        orderNotification.put("productId", String.valueOf(UUID.randomUUID()));
        orderNotification.put("orderId", null);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderNotificationMapper.toDTO(orderNotification)
        );

        String expectedMessage = "Required fields are missing in the order notification message.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toDTO_mapToOrderNotificationDTO() {
        Map<String, Object> orderNotification = new HashMap<>();
        orderNotification.put("orderConfirmed", false);
        orderNotification.put("orderStatus", "FAILED");
        orderNotification.put("productId", String.valueOf(UUID.randomUUID()));
        orderNotification.put("orderId", 1);

        OrderNotificationDTO orderNotificationDTO = orderNotificationMapper.toDTO(orderNotification);

        assertAll(() -> {
            assertNotNull(orderNotificationDTO);
            assertFalse(orderNotificationDTO.isOrderConfirmed());
            assertEquals("FAILED", orderNotificationDTO.getOrderStatus());
            assertEquals(orderNotification.get("productId"), orderNotificationDTO.getProductId());
            assertEquals(1, orderNotificationDTO.getOrderId());
        });
    }
}
