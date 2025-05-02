package yers.dev.products.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yers.dev.products.dto.ProductInventoryDto;
import yers.dev.products.dto.ProductsDto;
import yers.dev.products.dto.ResponseDto;
import yers.dev.products.model.Category;
import yers.dev.products.service.ProductsService;

import java.util.List;

import static yers.dev.products.constants.ProductsConstants.*;

@RequestMapping("/api/products")
@RestController
@AllArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping
    public ResponseEntity<List<ProductsDto>> getProducts() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productsService.getProducts());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createProduct(@Valid @RequestBody ProductInventoryDto productInventoryDto) {
        productsService.createProduct(productInventoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(MESSAGE_200, MESSAGE_201));
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateProduct(@Valid @RequestBody ProductInventoryDto productInventoryDto) {
        productsService.updateProduct(productInventoryDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(MESSAGE_200, MESSAGE_201_UPDATED_200));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductsDto>> getProductsByCategory(@PathVariable("category") Category category) {
        productsService.getProductsByCategory(category);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productsService.getProductsByCategory(category));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable("id") Long id) {
        productsService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(MESSAGE_200, STATUS_DELETED_200));
    }
    @GetMapping("/fetch/{id}")
    public ResponseEntity<ProductsDto> fetchProductDetails(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productsService.getProduct(id));
    }
}
