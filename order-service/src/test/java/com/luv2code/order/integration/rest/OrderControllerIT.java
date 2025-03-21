package com.luv2code.order.integration.rest;

import com.luv2code.order.integration.TestcontainersInitializer;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.dto.ProductCategoryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static reactor.core.publisher.Mono.just;

public class OrderControllerIT extends TestcontainersInitializer {

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
                .body(just(orderDTO), OrderDTO.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
