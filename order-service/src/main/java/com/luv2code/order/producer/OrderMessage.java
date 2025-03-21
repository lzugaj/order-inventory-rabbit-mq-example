package com.luv2code.order.producer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderMessage {

    @NotNull
    private UUID productId;

    @PositiveOrZero
    private int quantity;

    @NotNull
    private Long orderId;

}
