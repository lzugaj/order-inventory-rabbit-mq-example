package com.luv2code.order.consumer;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

@Component
public class OrderNotificationMapper {

    public OrderNotificationDTO toDTO(Map<String, Object> orderNotification) {
        Assert.notNull(orderNotification, "Order notification must not be null.");

        Object orderConfirmed = orderNotification.get("orderConfirmed");
        Object orderStatus = orderNotification.get("orderStatus");
        Object productId = orderNotification.get("productId");
        Object orderId = orderNotification.get("orderId");

        if (orderConfirmed == null || orderStatus == null || productId == null || orderId == null) {
            throw new IllegalArgumentException("Required fields are missing in the order notification message.");
        }

        return OrderNotificationDTO.builder()
                .orderConfirmed((boolean) orderConfirmed)
                .orderStatus((String) orderStatus)
                .productId((String) productId)
                .orderId((Integer) orderId)
                .build();
    }
}
