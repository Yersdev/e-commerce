package yers.dev.products.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yers.dev.products.model.dto.InventoryDto;

@FeignClient(name = "inventory", fallback = InventoryFallbacks.class)
public interface InventoryFeignClient {

    @PostMapping(value = "/api/inventory", consumes = "application/json")
    ResponseEntity<InventoryDto> createInventory(
            @RequestHeader("correlationId") String correlationId,
            @RequestBody InventoryDto inventoryDto
    );

    @PutMapping(value = "/api/inventory/update/{productId}", consumes = "application/json")
    ResponseEntity<InventoryDto> updateInventory(
            @RequestHeader("correlationId") String correlationId, @RequestBody InventoryDto inventoryDto,
            @PathVariable("productId") Long productId);

    @DeleteMapping(value = "/api/inventory/delete/{productId}")
    ResponseEntity<Void> deleteInventory(@RequestHeader("correlationId") String correlationId, @PathVariable("productId") Long productId);
}
