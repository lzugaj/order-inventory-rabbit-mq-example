package com.luv2code.order.consumer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderNotificationDTO {

    private boolean orderConfirmed;

    @NotBlank
    private String orderStatus;

    @NotNull
    private String productId;

    @NotNull
    private Integer orderId;

}
