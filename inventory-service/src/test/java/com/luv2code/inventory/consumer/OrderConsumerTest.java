package com.luv2code.inventory.consumer;

import com.luv2code.inventory.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderConsumerTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private OrderConsumer orderConsumer;

    @Test
    void processOrder_consumeOrderMessage_updateStock() {
        UUID uuid = UUID.randomUUID();

        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("productId", uuid);
        orderMessage.put("quantity", 2);

        OrderItem orderItem = OrderItem.builder()
                .productId((UUID) orderMessage.get("productId"))
                .quantity((Integer) orderMessage.get("quantity"))
                .build();

        when(orderMapper.toMessage(orderMessage)).thenReturn(orderItem);

        orderConsumer.processOrder(orderMessage);

        verify(orderMapper, times(1)).toMessage(any());
        verify(inventoryService, times(1)).updateStock(any(), any());
        verifyNoMoreInteractions(orderMapper, inventoryService);
    }
}
