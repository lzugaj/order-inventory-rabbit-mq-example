package com.luv2code.inventory.consumer;

import com.luv2code.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderMapper orderCreateMapper;
    private final InventoryService inventoryService;

    @RabbitListener(queues = "${com.luv2code.rabbitmq.queue.created}")
    public void processOrder(Map<String, Object> orderMessage) {
        log.info("Received new order for product. [productId={}]", orderMessage.get("productId"));

        OrderItem orderItem = orderCreateMapper.toMessage(orderMessage);
        Integer orderId = (Integer) orderMessage.get("orderId");
        inventoryService.updateStock(orderItem, orderId);
    }
}
