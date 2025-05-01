package yers.dev.inventory.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yers.dev.inventory.dto.InventoryDto;
import yers.dev.inventory.dto.ProductInventoryDto;
import yers.dev.inventory.dto.ResponseDto;
import yers.dev.inventory.service.InventoryService;

import java.util.List;

import static yers.dev.inventory.constants.InventoryConstants.*;

@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
@Validated
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryDto>> getInventory() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.fetchAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInventoryDto> fetchAllInventoryByProductId(@PathVariable("productId") Long productId) {
        inventoryService.fetchProduct(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.fetchProduct(productId));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ResponseDto> updateInventory(@RequestBody InventoryDto inventoryDto) {
        inventoryService.updateInventory(inventoryDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(STATUS_200, STATUS_UPDATED_200));
    }
    @PostMapping
    public ResponseEntity<ResponseDto> createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        try {
            inventoryService.addInventory(inventoryDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto(STATUS_200, MESSAGE_CREATED_200));
        } catch (Exception e) {
            log.error("❌ Error creating inventory: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Internal inventory error: " + e.getMessage()));
        }
    }

}
