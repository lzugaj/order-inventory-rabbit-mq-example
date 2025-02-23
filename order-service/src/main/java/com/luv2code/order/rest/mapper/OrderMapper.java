package com.luv2code.order.rest.mapper;

import com.luv2code.order.model.Order;
import com.luv2code.order.model.ProductCategory;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.dto.ProductCategoryDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class OrderMapper {

    public Order toEntity(OrderDTO orderDTO) {
        Assert.notNull(orderDTO, "Order DTO must not be null.");
        return Order.builder()
                .productName(orderDTO.getProductName())
                .productCategory(mapToProductCategory(orderDTO.getProductCategory()))
                .quantity(orderDTO.getQuantity())
                .price(orderDTO.getPrice())
                .build();
    }

    private ProductCategory mapToProductCategory(ProductCategoryDTO productCategoryDTO) {
        Assert.notNull(productCategoryDTO, "Product category DTO must not be null.");
        return ProductCategory.valueOf(productCategoryDTO.name());
    }
}
