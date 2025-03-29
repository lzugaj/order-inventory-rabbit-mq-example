package com.luv2code.inventory.integration.consumer;

import com.luv2code.inventory.consumer.OrderConsumer;
import com.luv2code.inventory.integration.BaseIntegrationTest;
import com.luv2code.inventory.model.Inventory;
import com.luv2code.inventory.producer.OrderNotificationDTO;
import com.luv2code.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

class OrderConsumerIT extends BaseIntegrationTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderConsumer orderConsumer;

    @Test
    void processOrder_nonExistingItem_thorwsIllegalArgumentException() {
        UUID nonExistingUUID = UUID.fromString("456e7890-1234-12d4-c789-912365421700");

        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", nonExistingUUID.toString());
        orderMessage.put("quantity", 2);
        orderMessage.put("orderId", 1);

        rabbitTemplate.convertAndSend("", orderCreatedQueue, orderMessage);

        assertThatThrownBy(() -> orderConsumer.processOrder(orderMessage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product is not find in inventory. [productId=%s]".formatted(nonExistingUUID));
    }

    @Test
    void processOrder_itemNotAvailable_sendFailedOrderNotificationMessage() {
        UUID existingUUID = UUID.fromString("456e7890-b345-12d4-c789-912365421700");

        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", existingUUID);
        orderMessage.put("quantity", 2);
        orderMessage.put("orderId", 1);

        rabbitTemplate.convertAndSend("", orderCreatedQueue, orderMessage);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            Object message = rabbitTemplate.receiveAndConvert(orderFailedQueue);

            assertNotNull(message);
            assertInstanceOf(OrderNotificationDTO.class, message);

            OrderNotificationDTO orderNotificationDTO = (OrderNotificationDTO) message;
            assertNotNull(orderNotificationDTO);
            assertEquals("FAILED", orderNotificationDTO.getOrderStatus());
            assertEquals(existingUUID, UUID.fromString(orderNotificationDTO.getProductId()));
            assertEquals(1, orderNotificationDTO.getOrderId());
            assertFalse(orderNotificationDTO.isOrderConfirmed());
        });
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    void processOrder_itemIsAvailable_sendConfirmedOrderNotificationMessage() {
        UUID existingUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", existingUUID);
        orderMessage.put("quantity", 1);
        orderMessage.put("orderId", 1);

        rabbitTemplate.convertAndSend("", orderCreatedQueue, orderMessage);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            Optional<Inventory> inventory = inventoryRepository.findByProductId(existingUUID);
            assertTrue(inventory.isPresent());
            assertEquals(99, inventory.get().getQuantityInStock());

            Object message = rabbitTemplate.receiveAndConvert(orderConfirmedQueue);
            assertNotNull(message);
            assertInstanceOf(OrderNotificationDTO.class, message);

            OrderNotificationDTO orderNotificationDTO = (OrderNotificationDTO) message;
            assertNotNull(orderNotificationDTO);
            assertEquals("CONFIRMED", orderNotificationDTO.getOrderStatus());
            assertEquals(existingUUID, UUID.fromString(orderNotificationDTO.getProductId()));
            assertEquals(1, orderNotificationDTO.getOrderId());
            assertTrue(orderNotificationDTO.isOrderConfirmed());
        });
    }
}
