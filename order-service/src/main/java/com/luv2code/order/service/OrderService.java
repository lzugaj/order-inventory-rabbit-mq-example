package com.luv2code.order.service;

import com.luv2code.order.model.Order;
import com.luv2code.order.producer.OrderCreateProducer;
import com.luv2code.order.producer.OrderCreatedMapper;
import com.luv2code.order.producer.OrderCreatedMessage;
import com.luv2code.order.repository.OrderRepository;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderCreateProducer orderCreateProducer;
    private final OrderCreatedMapper orderCreatedMapper;

    @Transactional
    public void makeOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);

        OrderCreatedMessage message = orderCreatedMapper.toMessage(orderDTO);
        orderCreateProducer.makeOrder(message);
    }
}
