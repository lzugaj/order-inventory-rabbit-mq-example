package com.luv2code.order.mapper;

import com.luv2code.order.model.Order;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.luv2code.order.model.OrderStatus.CREATED;
import static com.luv2code.order.rest.dto.ProductCategoryDTO.CLOTHING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @InjectMocks
    private OrderMapper orderMapper;

    @Test
    void toEntity_wholeObjectIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderMapper.toEntity(null)
        );

        String expectedMessage = "Order DTO must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toEntity_productCategoryIsNull_throwsIllegalArgumentException() {
        OrderDTO orderDTO = OrderDTO.builder()
                .productId(UUID.randomUUID())
                .productName("Jeans")
                .productCategory(null)
                .quantity(1)
                .price(BigDecimal.TEN)
                .build();

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderMapper.toEntity(orderDTO)
        );

        String expectedMessage = "Product category DTO must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void toEntity_mapDtoToEntity() {
        OrderDTO orderDTO = OrderDTO.builder()
                .productId(UUID.randomUUID())
                .productName("Jeans")
                .productCategory(CLOTHING)
                .quantity(1)
                .price(BigDecimal.TEN)
                .build();

        Order order = orderMapper.toEntity(orderDTO);

        assertAll(() -> {
            assertNotNull(order);
            assertEquals(CREATED, order.getOrderStatus());
        });
    }
}
