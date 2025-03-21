package com.luv2code.inventory.consumer;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderMapper {

    public OrderItem toMessage(Map<String, Object> orderMessage) {
        Assert.notNull(orderMessage, "Order message must not be null.");

        Object productId = orderMessage.get("productId");
        Object quantity = orderMessage.get("quantity");

        if (productId == null || quantity == null) {
            throw new IllegalArgumentException("Required fields are missing in the order message.");
        }

        return OrderItem.builder()
                .productId(UUID.fromString((String) productId))
                .quantity((Integer) quantity)
                .build();
    }
}