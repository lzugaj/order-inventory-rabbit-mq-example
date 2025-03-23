package com.luv2code.order.producer;

import com.luv2code.order.rest.dto.OrderDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.luv2code.order.rest.dto.ProductCategoryDTO.CLOTHING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderCreateMapperTest {

    @InjectMocks
    private OrderCreateMapper orderCreateMapper;

    @Test
    void toMessage_null_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderCreateMapper.toMessage(null, 1L)
        );

        String expectedMessage = "Order DTO must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toMessage_createOrderMessage() {
        OrderDTO orderDTO = OrderDTO.builder()
                .productId(UUID.randomUUID())
                .productName("Jeans")
                .productCategory(CLOTHING)
                .quantity(1)
                .price(BigDecimal.TEN)
                .build();

        Long orderId = 1L;

        OrderMessage message = orderCreateMapper.toMessage(orderDTO, orderId);

        assertAll(() -> {
            assertNotNull(message);
            assertEquals(orderDTO.getProductId(), message.getProductId());
            assertEquals(orderDTO.getQuantity(), message.getQuantity());
            assertEquals(orderId, message.getOrderId());
        });
    }
}
