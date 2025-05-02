package yers.dev.products.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import yers.dev.products.dto.InventoryDto;

@FeignClient(name = "inventory", fallback = InventoryFallbacks.class)
public interface InventoryFeignClient {

    @PostMapping(value = "/api/inventory", consumes = "application/json")
    ResponseEntity<InventoryDto> createInventory(
            @RequestHeader("correlationId") String correlationId,
            @RequestBody InventoryDto inventoryDto
    );

    @PutMapping(value = "/api/inventory", consumes = "application/json")
    ResponseEntity<InventoryDto> updateInventory(
            @RequestHeader("correlationId") String correlationId,
            @RequestBody InventoryDto inventoryDto);
}
