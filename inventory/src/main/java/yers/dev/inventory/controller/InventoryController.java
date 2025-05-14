package yers.dev.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yers.dev.inventory.entity.dto.ErrorResponseDto;
import yers.dev.inventory.entity.dto.InventoryDto;
import yers.dev.inventory.entity.dto.ProductInventoryDto;
import yers.dev.inventory.entity.dto.ResponseDto;
import yers.dev.inventory.service.InventoryService;
import java.util.List;
import static yers.dev.inventory.constants.InventoryConstants.*;

@Tag(name = "Inventory", description = "Inventory API")
@RestController
@RequestMapping("/api/inventory")
@AllArgsConstructor
@Validated
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(
            summary = "Fetch All Products in Database",
            description = "REST API to fetch All Products in Database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping
    public ResponseEntity<List<InventoryDto>> getInventory() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.fetchAllProducts());
    }

    @Operation(
            summary = "Fetch Product by product id",
            description = "REST API to fetch product by product id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/{productId}")
    public ResponseEntity<ProductInventoryDto> fetchAllInventoryByProductId(@PathVariable("productId") Long productId) {
        inventoryService.fetchProduct(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryService.fetchProduct(productId));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ResponseDto> updateInventory(@PathVariable("productId") Long productId, @RequestBody InventoryDto inventoryDto) {
        inventoryService.updateInventory(productId, inventoryDto);
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

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ResponseDto> deleteInventory(@PathVariable("productId") Long productId) {
        try {
            inventoryService.deleteInventory(productId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(STATUS_200, STATUS_DELETED_200));
        } catch (Exception e) {
            log.error("❌ Error deleting inventory: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("500", "Internal inventory error: " + e.getMessage()));
        }
    }

}
