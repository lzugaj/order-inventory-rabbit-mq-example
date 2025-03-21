package com.luv2code.inventory.service;

import com.luv2code.inventory.consumer.OrderItem;
import com.luv2code.inventory.model.Inventory;
import com.luv2code.inventory.producer.OrderNotificationProducer;
import com.luv2code.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final OrderNotificationProducer orderNotificationProducer;

    @Transactional
    public void updateStock(OrderItem orderItem, Integer orderId) {
        boolean itemOutOfStock = checkItemAvailability(orderItem);
        if (itemOutOfStock) {
            orderNotificationProducer.sendFailedOrderNotificationMessage(orderItem.getProductId(), orderId);
        } else {
            Inventory inventory = getInventory(orderItem.getProductId());
            inventory.updateProductQuantityStock(inventory.getQuantityInStock() - orderItem.getQuantity());
            inventoryRepository.save(inventory);

            orderNotificationProducer.sendConfirmedOrderNotificationMessage(orderItem.getProductId(), orderId);
            log.info("Successfully updated product stock. Order confirmed. [productId={}]", orderItem.getProductId());
        }
    }

    private boolean checkItemAvailability(OrderItem orderItem) {
        boolean outOfStock = false;
        Inventory inventory = getInventory(orderItem.getProductId());
        if (isItemAvailable(inventory, orderItem)) {
            log.error("Product is not available currently. Order failed. [productId={}]", orderItem.getProductId());
            outOfStock = true;
        }

        return outOfStock;
    }

    private boolean isItemAvailable(Inventory inventory, OrderItem item) {
        return !inventory.isAvailable() || (inventory.getQuantityInStock() < item.getQuantity());
    }

    private Inventory getInventory(UUID productId) {
        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
        if (inventory.isEmpty()) {
            log.error("Product is not find in inventory. Order failed. [productId={}]", productId);
            throw new IllegalArgumentException(
                    String.format("Product is not find in inventory. [productId=%s]", productId)
            );
        }

        return inventory.get();
    }
}
