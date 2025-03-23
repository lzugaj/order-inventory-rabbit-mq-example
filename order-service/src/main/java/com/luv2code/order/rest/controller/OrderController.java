package com.luv2code.order.rest.controller;

import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @ResponseStatus(CREATED)
    @PostMapping("/make-order")
    private void makeOrder(@Valid @RequestBody OrderDTO orderDTO) {
        orderService.makeOrder(orderDTO);
    }
}
