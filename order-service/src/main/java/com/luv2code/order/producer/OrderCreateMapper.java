package com.luv2code.order.producer;

import com.luv2code.order.rest.dto.OrderDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class OrderCreateMapper {

    public OrderMessage toMessage(OrderDTO orderDTO, long orderId) {
        Assert.notNull(orderDTO, "Order DTO must not be null.");
        return OrderMessage.builder()
                .productId(orderDTO.getProductId())
                .quantity(orderDTO.getQuantity())
                .orderId(orderId)
                .build();
    }
}
