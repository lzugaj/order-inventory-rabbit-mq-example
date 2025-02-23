package com.luv2code.order.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderDTO {

    @NotNull
    private UUID productId;

    @NotBlank
    private String productName;

    @NotNull
    private ProductCategoryDTO productCategory;

    @PositiveOrZero
    private int quantity;

    @PositiveOrZero
    private BigDecimal price;

}
