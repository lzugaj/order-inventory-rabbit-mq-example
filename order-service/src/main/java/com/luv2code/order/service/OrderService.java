package com.luv2code.order.service;

import com.luv2code.order.consumer.OrderNotificationDTO;
import com.luv2code.order.model.Order;
import com.luv2code.order.producer.OrderCreateMapper;
import com.luv2code.order.producer.OrderMessage;
import com.luv2code.order.producer.OrderProducer;
import com.luv2code.order.repository.OrderRepository;
import com.luv2code.order.rest.dto.OrderDTO;
import com.luv2code.order.rest.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.luv2code.order.model.OrderStatus.CONFIRMED;
import static com.luv2code.order.model.OrderStatus.FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderProducer orderProducer;
    private final OrderCreateMapper orderCreateMapper;

    @Transactional
    public void makeOrder(OrderDTO orderDTO) {
        log.info("Start process of making order for product. [productId={}]", orderDTO.getProductId());
        Order order = orderMapper.toEntity(orderDTO);
        orderRepository.save(order);

        OrderMessage message = orderCreateMapper.toMessage(orderDTO, order.getId());
        orderProducer.makeOrder(message);
        log.info("Finish process of making order for product. [productId={}]", orderDTO.getProductId());
    }

    @Transactional
    public void processOrder(OrderNotificationDTO orderNotificationDTO) {
        Optional<Order> order = orderRepository.findById(Long.valueOf(orderNotificationDTO.getOrderId()));
        if (order.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Cannot find searched order. [orderId=%s]", orderNotificationDTO.getOrderId())
            );
        }

        if (FAILED.name().equals(orderNotificationDTO.getOrderStatus())) {
            order.get().orderFailed();
        } else if (CONFIRMED.name().equals(orderNotificationDTO.getOrderStatus())) {
            order.get().orderConfirmed();
        }
    }
}
