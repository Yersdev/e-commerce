package yers.dev.products.service.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import yers.dev.products.dto.InventoryDto;

@Component
public class InventoryFallbacks implements InventoryFeignClient {
    @Override
    public ResponseEntity<InventoryDto> createInventory(String correlationId, InventoryDto inventoryDto) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @Override
    public ResponseEntity<InventoryDto> updateInventory(String correlationId, InventoryDto inventoryDto, Long productId) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}
