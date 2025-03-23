package com.luv2code.order.integration.controller;

import com.luv2code.order.integration.BaseIntegrationTest;
import com.luv2code.order.producer.OrderMessage;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.dto.ProductCategoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerIT extends BaseIntegrationTest {

    @Test
    @DisplayName("500 - POST /api/v1/orders/make-order")
    void pendingOtherAbsencesDays_shouldReturnPendingOtherAbsencesDaysDTO_throwException() {
        OrderDTO orderDTO = OrderDTO.builder()
                .productId(UUID.randomUUID())
                .productName(null)
                .productCategory(ProductCategoryDTO.CLOTHING)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();

        webTestClient.post()
                .uri("/api/v1/orders/make-order")
                .body(Mono.just(orderDTO), OrderDTO.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @DisplayName("201 - POST /api/v1/orders/make-order")
    void pendingOtherAbsencesDays_shouldReturnPendingOtherAbsencesDaysDTO() {
        OrderDTO orderDTO = OrderDTO.builder()
                .productId(UUID.randomUUID())
                .productName("Jeans")
                .productCategory(ProductCategoryDTO.CLOTHING)
                .quantity(2)
                .price(BigDecimal.valueOf(50.00))
                .build();

        webTestClient.post()
                    .uri("/api/v1/orders/make-order")
                .body(Mono.just(orderDTO), OrderDTO.class)
                .exchange()
                .expectStatus()
                .isCreated();

        Object message = rabbitTemplate.receiveAndConvert(orderQueue, 3000);
        assertAll(() -> {
            assertNotNull(message);
            assertInstanceOf(OrderMessage.class, message);

            OrderMessage orderMessage = (OrderMessage) message;
            assertEquals(orderDTO.getProductId(), orderMessage.getProductId());
            assertEquals(((OrderMessage) message).getOrderId(), orderMessage.getOrderId());
        });
    }
}