package com.luv2code.order.service;

import com.luv2code.order.consumer.OrderNotificationDTO;
import com.luv2code.order.model.Order;
import com.luv2code.order.model.ProductCategory;
import com.luv2code.order.producer.OrderCreateMapper;
import com.luv2code.order.producer.OrderMessage;
import com.luv2code.order.producer.OrderProducer;
import com.luv2code.order.repository.OrderRepository;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.dto.ProductCategoryDTO;
import com.luv2code.order.rest.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.luv2code.order.model.OrderStatus.CONFIRMED;
import static com.luv2code.order.model.OrderStatus.FAILED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private OrderCreateMapper orderCreateMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void makeOrder_validDTO_produceMessage() {
        OrderDTO orderDTO = OrderDTO.builder()
                .productId(UUID.randomUUID())
                .productName("Jeans")
                .productCategory(ProductCategoryDTO.CLOTHING)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();

        Order order = Order.builder()
                .productName("Jeans")
                .productCategory(ProductCategory.CLOTHING)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();
        ReflectionTestUtils.setField(order, "id", 5L);

        OrderMessage orderMessage = OrderMessage.builder()
                .productId(orderDTO.getProductId())
                .quantity(orderDTO.getQuantity())
                .orderId(order.getId())
                .build();

        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(orderCreateMapper.toMessage(orderDTO, order.getId())).thenReturn(orderMessage);

        orderService.makeOrder(orderDTO);

        verify(orderRepository, times(1)).save(any());
        verify(orderProducer, times(1)).makeOrder(any());
    }

    @Test
    void processOrder_emptyOrder_throwsIllegalArgumentException() {
        OrderNotificationDTO orderNotificationDTO = OrderNotificationDTO.builder()
                .orderConfirmed(true)
                .orderStatus("CANCELED")
                .productId("12")
                .orderId(134)
                .build();

        when(orderRepository.findById(Long.valueOf(orderNotificationDTO.getOrderId()))).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.processOrder(orderNotificationDTO)
        );

        String expectedMessage = "Cannot find searched order. [orderId=134]";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void processOrder_foundFailedOrder_setOrderStatus() {
        OrderNotificationDTO orderNotificationDTO = OrderNotificationDTO.builder()
                .orderConfirmed(true)
                .orderStatus("FAILED")
                .productId("12")
                .orderId(134)
                .build();

        Order order = Order.builder()
                .productName("Jeans")
                .productCategory(ProductCategory.CLOTHING)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();
        ReflectionTestUtils.setField(order, "id", 134L);

        when(orderRepository.findById(Long.valueOf(orderNotificationDTO.getOrderId()))).thenReturn(Optional.of(order));

        orderService.processOrder(orderNotificationDTO);

        assertEquals(FAILED, order.getOrderStatus());
        verify(orderRepository, times(1)).findById(134L);
    }

    @Test
    void processOrder_foundConfirmedOrder_setOrderStatus() {
        OrderNotificationDTO orderNotificationDTO = OrderNotificationDTO.builder()
                .orderConfirmed(true)
                .orderStatus("CONFIRMED")
                .productId("12")
                .orderId(134)
                .build();

        Order order = Order.builder()
                .productName("Jeans")
                .productCategory(ProductCategory.CLOTHING)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();
        ReflectionTestUtils.setField(order, "id", 134L);

        when(orderRepository.findById(Long.valueOf(orderNotificationDTO.getOrderId()))).thenReturn(Optional.of(order));

        orderService.processOrder(orderNotificationDTO);

        assertEquals(CONFIRMED, order.getOrderStatus());
        verify(orderRepository, times(1)).findById(134L);
    }
}
