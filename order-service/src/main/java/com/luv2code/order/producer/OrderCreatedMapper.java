package com.luv2code.order.producer;

import com.luv2code.order.rest.dto.OrderDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class OrderCreatedMapper {

    public OrderCreatedMessage toMessage(OrderDTO orderDTO) {
        Assert.notNull(orderDTO, "Order DTO must not be null.");

        return OrderCreatedMessage.builder()
                .productId(orderDTO.getProductId())
                .quantity(orderDTO.getQuantity())
                .build();
    }
}
