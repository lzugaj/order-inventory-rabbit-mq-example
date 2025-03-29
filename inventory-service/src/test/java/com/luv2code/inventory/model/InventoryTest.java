package com.luv2code.inventory.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void buildOrder_validObject_create() {
        Inventory inventory = Inventory.builder()
                .productId(UUID.randomUUID())
                .available(true)
                .price(TEN)
                .quantityInStock(2)
                .build();

        assertNotNull(inventory);
    }

    @Test
    void updateProductQuantityStock_notEnoughInStock_throwsIllegalArgumentException() {
        Inventory inventory = Inventory.builder()
                .productId(UUID.randomUUID())
                .available(true)
                .price(TEN)
                .quantityInStock(2)
                .build();

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventory.updateProductQuantityStock(4)
        );

        String expectedMessage = "Cannot have less then 0 product in inventory.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void updateProductQuantityStock_deductFromStock() {
        Inventory inventory = Inventory.builder()
                .productId(UUID.randomUUID())
                .available(true)
                .price(TEN)
                .quantityInStock(2)
                .build();

        inventory.updateProductQuantityStock(1);

        assertEquals(1, inventory.getQuantityInStock());
    }
}
