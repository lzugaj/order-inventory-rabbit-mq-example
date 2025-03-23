package com.luv2code.order.integration.consumer;

import com.luv2code.order.integration.BaseIntegrationTest;
import com.luv2code.order.model.Order;
import com.luv2code.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.luv2code.order.model.OrderStatus.CONFIRMED;
import static com.luv2code.order.model.OrderStatus.FAILED;
import static com.luv2code.order.model.ProductCategory.CLOTHING;
import static java.math.BigDecimal.TEN;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

class OrderNotificationConsumerIT extends BaseIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    void failedOrderNotification_setOrderStatusToFailed() {
        Order order = new Order("Jeans", CLOTHING, 2, TEN);
        orderRepository.saveAndFlush(order);

        assertAll(() -> {
            Optional<Order> searchedOrder = orderRepository.findById(order.getId());
            assertTrue(searchedOrder.isPresent());
        });

        Map<String, Object> message = Map.of(
                "orderConfirmed", false,
                "orderStatus", "FAILED",
                "productId", UUID.randomUUID().toString(),
                "orderId", order.getId()
        );

        rabbitTemplate.convertAndSend("", orderFailedQueue, message);

        await().atMost(3, SECONDS).untilAsserted(() -> {
            Order searchedOrder = orderRepository.findById(order.getId()).get();
            assertEquals(FAILED, searchedOrder.getOrderStatus());
        });
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    void confirmedOrderNotification_setOrderStatusToConfirmed() {
        Order order = new Order("Jeans", CLOTHING, 2, TEN);
        orderRepository.saveAndFlush(order);

        assertAll(() -> {
            Optional<Order> searchedOrder = orderRepository.findById(order.getId());
            assertTrue(searchedOrder.isPresent());
        });

        Map<String, Object> message = Map.of(
                "orderConfirmed", true,
                "orderStatus", "CONFIRMED",
                "productId", UUID.randomUUID().toString(),
                "orderId", order.getId()
        );

        rabbitTemplate.convertAndSend("", orderConfirmedQueue, message);

        await().atMost(3, SECONDS).untilAsserted(() -> {
            Order searchedOrder = orderRepository.findById(order.getId()).get();
            assertEquals(CONFIRMED, searchedOrder.getOrderStatus());
        });
    }
}
