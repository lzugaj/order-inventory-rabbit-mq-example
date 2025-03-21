package com.luv2code.order.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.luv2code.order.model.OrderStatus.*;
import static com.luv2code.order.model.ProductCategory.CLOTHING;
import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void buildOrder_productIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Order.builder()
                        .productName(null)
                        .productCategory(CLOTHING)
                        .quantity(1)
                        .price(BigDecimal.valueOf(50))
                        .build()
        );

        String expectedMessage = "Product name must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void buildOrder_productCategoryIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Order.builder()
                        .productName("Jeans")
                        .productCategory(null)
                        .quantity(1)
                        .price(BigDecimal.valueOf(50))
                        .build()
        );

        String expectedMessage = "Product category must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void buildOrder_productPriceIsNull_throwsIllegalArgumentException() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> Order.builder()
                        .productName("Jeans")
                        .productCategory(CLOTHING)
                        .quantity(1)
                        .price(null)
                        .build()
        );

        String expectedMessage = "Price must not be null.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void buildOrder_validObject_create() {
        Order order = Order.builder()
                .productName("Jeans")
                .productCategory(CLOTHING)
                .quantity(1)
                .price(ONE)
                .build();

        assertEquals(CREATED, order.getOrderStatus());
    }

    @Test
    void orderFailed_setStatusToFailed() {
        Order order = Order.builder()
                .productName("Jeans")
                .productCategory(CLOTHING)
                .quantity(1)
                .price(ONE)
                .build();

        order.orderFailed();

        assertEquals(FAILED, order.getOrderStatus());
    }

    @Test
    void orderConfirmed_setStatusToConfirmed() {
        Order order = Order.builder()
                .productName("Jeans")
                .productCategory(CLOTHING)
                .quantity(1)
                .price(ONE)
                .build();

        order.orderConfirmed();

        assertEquals(CONFIRMED, order.getOrderStatus());
    }
}
