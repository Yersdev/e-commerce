package yers.dev.products.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import yers.dev.products.dto.InventoryDto;
import yers.dev.products.dto.ProductInventoryDto;
import yers.dev.products.dto.ProductsDto;
import yers.dev.products.mapper.ProductInventoryMapper;
import yers.dev.products.mapper.ProductsMapper;
import yers.dev.products.model.Category;
import yers.dev.products.repository.ProductsRepository;
import yers.dev.products.service.client.InventoryFeignClient;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ProductsMapper productsMapper;
    private final InventoryFeignClient inventoryFeignClient;

    public List<ProductsDto> getProducts() {
        return productsMapper.toProductsDto(productsRepository.findAll());
    }

    @Transactional
    public ProductsDto createProduct(ProductInventoryDto productInventoryDto) {

        InventoryDto inventoryDto = ProductInventoryMapper.toInventoryDto(productInventoryDto);
        ProductsDto productsDto = ProductInventoryMapper.toProductsDto(productInventoryDto);

        ResponseEntity<InventoryDto> response = inventoryFeignClient.createInventory(
                "correlationId", inventoryDto
        );

        if (response == null ||!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Inventory creation failed: " + response.getStatusCode());
        }

        return productsMapper.toProductsDto(
                productsRepository.save(productsMapper.toProducts(productsDto))
        );
    }


    @Transactional
    public ProductsDto updateProduct(ProductInventoryDto productInventoryDto) {
        InventoryDto inventoryDto = ProductInventoryMapper.toInventoryDto(productInventoryDto);
        ProductsDto productsDto = ProductInventoryMapper.toProductsDto(productInventoryDto);

        ResponseEntity<InventoryDto> response = inventoryFeignClient.updateInventory(
            "correlationId", inventoryDto
        );

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Inventory update failed: " + response.getStatusCode());
        }
        return productsMapper.toProductsDto(productsRepository.save(productsMapper.toProducts(productsDto)));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }

    public ProductsDto getProduct(Long id) {
        return productsMapper.toProductsDto(productsRepository.findById(id).orElse(null));
    }

    public List<ProductsDto> getProductsByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }

        return switch (category) {
            case ELECTRONICS, CLOTHING, BOOKS, TOYS, FURNITURE ->
                    productsMapper.toProductsDto(List.of(productsRepository.findByCategory(category).orElseThrow(() -> new RuntimeException())));
        };
    }
}
