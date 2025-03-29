package com.luv2code.inventory.service;

import com.luv2code.inventory.consumer.OrderItem;
import com.luv2code.inventory.model.Inventory;
import com.luv2code.inventory.producer.OrderNotificationProducer;
import com.luv2code.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private OrderNotificationProducer orderNotificationProducer;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void updateStock_itemNotExisting_throwsIllegalArgumentException() {
        OrderItem orderItem = OrderItem.builder()
                .productId(UUID.randomUUID())
                .quantity(3)
                .build();

        int orderId = 1;

        when(inventoryRepository.findByProductId(orderItem.getProductId()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.updateStock(orderItem, orderId)
        );

        String expectedMessage = String.format("Product is not find in inventory. [productId=%s]", orderItem.getProductId());
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(inventoryRepository, times(1)).findByProductId(any());
        verify(orderNotificationProducer, times(0)).sendFailedOrderNotificationMessage(any(), any());
        verify(orderNotificationProducer, times(0)).sendConfirmedOrderNotificationMessage(any(), any());
        verifyNoMoreInteractions(inventoryRepository);
        verifyNoInteractions(orderNotificationProducer);
    }

    @Test
    void updateStock_itemOutOfStock_sendFailedOrderNotificationMessage() {
        OrderItem orderItem = OrderItem.builder()
                .productId(UUID.randomUUID())
                .quantity(3)
                .build();

        int orderId = 1;

        Inventory inventory = Inventory.builder()
                .productId(orderItem.getProductId())
                .available(true)
                .price(TEN)
                .quantityInStock(2)
                .build();

        when(inventoryRepository.findByProductId(orderItem.getProductId()))
                .thenReturn(Optional.of(inventory));

        inventoryService.updateStock(orderItem, orderId);

        verify(inventoryRepository, times(1)).findByProductId(any());
        verify(orderNotificationProducer, times(1)).sendFailedOrderNotificationMessage(any(), any());
        verify(orderNotificationProducer, times(0)).sendConfirmedOrderNotificationMessage(any(), any());
        verifyNoMoreInteractions(inventoryRepository, orderNotificationProducer);
    }

    @Test
    void updateStock_deductOutOfStock_sendConfirmedOrderNotificationMessage() {
        OrderItem orderItem = OrderItem.builder()
                .productId(UUID.randomUUID())
                .quantity(1)
                .build();

        int orderId = 1;

        Inventory inventory = Inventory.builder()
                .productId(orderItem.getProductId())
                .available(true)
                .price(TEN)
                .quantityInStock(2)
                .build();

        when(inventoryRepository.findByProductId(orderItem.getProductId()))
                .thenReturn(Optional.of(inventory));

        inventoryService.updateStock(orderItem, orderId);

        assertEquals(1, inventory.getQuantityInStock());

        verify(inventoryRepository, times(2)).findByProductId(any());
        verify(orderNotificationProducer, times(0)).sendFailedOrderNotificationMessage(any(), any());
        verify(orderNotificationProducer, times(1)).sendConfirmedOrderNotificationMessage(any(), any());
        verifyNoMoreInteractions(inventoryRepository, orderNotificationProducer);
    }
}
