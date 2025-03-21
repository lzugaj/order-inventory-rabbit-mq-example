package com.luv2code.order.consumer;

import com.luv2code.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderNotificationConsumer {

    private final OrderNotificationMapper orderNotificationMapper;
    private final OrderService orderService;

    @RabbitListener(queues = "${com.luv2code.rabbitmq.queue.failed}")
    public void failedOrderNotification(Map<String, Object> failedNotification) {
        log.info("Received failed order notification for product. [productId={}]", failedNotification.get("productId"));
        OrderNotificationDTO orderNotificationDTO = orderNotificationMapper.toDTO(failedNotification);
        orderService.processOrder(orderNotificationDTO);
    }

    @RabbitListener(queues = "${com.luv2code.rabbitmq.queue.confirmed}")
    public void confirmedOrderNotification(Map<String, Object> confirmedNotification) {
        log.info("Received confirmed order notification for product. [productId={}]", confirmedNotification.get("productId"));
        OrderNotificationDTO orderNotificationDTO = orderNotificationMapper.toDTO(confirmedNotification);
        orderService.processOrder(orderNotificationDTO);
    }
}
