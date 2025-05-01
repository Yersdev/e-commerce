//package yers.dev.inventory.controller;
//
//import lombok.AllArgsConstructor;
//import org.springframework.cloud.client.loadbalancer.Response;
//import org.springframework.data.domain.jaxb.SpringDataJaxb;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.service.annotation.PutExchange;
//import yers.dev.inventory.dto.InventoryDto;
//import yers.dev.inventory.dto.ResponseDto;
//import yers.dev.inventory.service.InventoryService;
//
//import static yers.dev.inventory.constants.InventoryConstants.MESSAGE_200;
//
//@RestController
//@RequestMapping("/api/inventory")
//@AllArgsConstructor
//public class InventoryController {
//
//    private final InventoryService inventoryService;
//
//    @GetMapping
//    public ResponseEntity<InventoryDto> getInventory() {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(inventoryService.fetchAllProducts());
//    }
//
//    @GetMapping("/{productId}")
//    public ResponseEntity<InventoryDto> fetchAllInventoryByProductId(@PathVariable("productId") Long productId) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(inventoryService.fetchProduct(productId));
//    }
//
//    @PutMapping("/update/{productId}")
//    public Response<ResponseDto> updateInventory(@PathVariable("productId") Long productId) {
//        inventoryService.updateInventory(productId);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ResponseDto(MESSAGE_200, STATUS_UPDATED_200));
//    }
//
//
//}
