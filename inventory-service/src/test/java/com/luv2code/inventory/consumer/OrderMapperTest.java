package com.luv2code.inventory.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @InjectMocks
    private OrderMapper orderMapper;

    @Test
    void toMessage_nullObject_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderMapper.toMessage(null)
        );

        String expectedMessage = "Order message must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toMessage_productIdIsNull_throwsIllegalArgumentException() {
        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", null);
        orderMessage.put("quantity", 2);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderMapper.toMessage(orderMessage)
        );

        String expectedMessage = "Required fields are missing in the order message.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toMessage_quantityIsNull_throwsIllegalArgumentException() {
        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", 1);
        orderMessage.put("quantity", null);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderMapper.toMessage(orderMessage)
        );

        String expectedMessage = "Required fields are missing in the order message.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toMessage_validObject_returnOrderItem() {
        UUID uuid = UUID.randomUUID();

        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", String.valueOf(uuid));
        orderMessage.put("quantity", 2);

        OrderItem message = orderMapper.toMessage(orderMessage);

        assertAll(() -> {
            assertNotNull(message);
            assertEquals(uuid, message.getProductId());
            assertEquals(2, message.getQuantity());
        });
    }
}
