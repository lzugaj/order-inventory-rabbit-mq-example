package com.luv2code.inventory.integration.repository;

import com.luv2code.inventory.integration.BaseIntegrationTest;
import com.luv2code.inventory.model.Inventory;
import com.luv2code.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void findByProductId_returnInventory() {
        UUID existingUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Optional<Inventory> searchedInventory = inventoryRepository.findByProductId(existingUUID);

        assertTrue(searchedInventory.isPresent());
    }

    @Test
    void findByProductId_returnEmpty() {
        UUID nonExistingUUID = UUID.fromString("123e4567-1111-12d3-a456-426614174000");
        Optional<Inventory> searchedInventory = inventoryRepository.findByProductId(nonExistingUUID);

        assertTrue(searchedInventory.isEmpty());
    }
}
