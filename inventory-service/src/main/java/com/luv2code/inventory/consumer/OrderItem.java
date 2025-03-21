package com.luv2code.inventory.consumer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderItem {

    @NotNull
    private UUID productId;

    @PositiveOrZero
    private int quantity;

}
