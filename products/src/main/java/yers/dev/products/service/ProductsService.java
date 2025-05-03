package yers.dev.products.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import yers.dev.products.model.dto.InventoryDto;
import yers.dev.products.model.dto.ProductInventoryDto;
import yers.dev.products.model.dto.ProductsDto;
import yers.dev.products.mapper.ProductInventoryMapper;
import yers.dev.products.mapper.ProductsManualMapper;
import yers.dev.products.model.Category;
import yers.dev.products.model.Products;
import yers.dev.products.repository.ProductsRepository;
import yers.dev.products.service.client.InventoryFeignClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final InventoryFeignClient inventoryFeignClient;
    private final ProductsManualMapper productsManualMapper;

    public List<ProductsDto> getProducts() {
        return productsManualMapper.toDtoList((productsRepository.findAll()));
    }

    @Transactional
    public ProductsDto createProduct(ProductInventoryDto productInventoryDto) {

        log.info("ProductInventoryDto: {}", productInventoryDto);
        InventoryDto inventoryDto = ProductInventoryMapper.toInventoryDto(productInventoryDto);

        ProductsDto productsDto = ProductInventoryMapper.toProductsDto(productInventoryDto);

        log.info("InventoryDto: {}", inventoryDto);
        ResponseEntity<InventoryDto> response = inventoryFeignClient.createInventory(
                "correlationId", inventoryDto
        );

        if (response == null ||!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Inventory creation failed: " + response.getStatusCode());
        }
        log.info("ProductDto: {}", productsDto);
        log.info("ProductsMapper: {}", productsManualMapper.toEntity(productsDto));
        return productsManualMapper.toDto(
                productsRepository.save(productsManualMapper.toEntity(productsDto))
        );
    }


    @Transactional
    public ProductsDto updateProduct(Long id, ProductInventoryDto productInventoryDto) {
        InventoryDto inventoryDto = ProductInventoryMapper.toInventoryDto(productInventoryDto);
        ProductsDto productsDto = ProductInventoryMapper.toProductsDto(productInventoryDto);

        Products products = productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        products.setName(productsDto.getName());
        products.setDescription(productsDto.getDescription());
        products.setCategory(productsDto.getCategory());
        products.setPrice(productsDto.getPrice());
        products.setUpdatedAt(LocalDateTime.now());
        products.setActive(true);

        ResponseEntity<InventoryDto> response = inventoryFeignClient.updateInventory(
            "correlationId", inventoryDto, id
        );

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Inventory update failed: " + response.getStatusCode());
        }
        return productsManualMapper.toDto(productsRepository.save(products));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!inventoryFeignClient.deleteInventory("correlationId", id).getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Inventory deletion failed");
        }
        productsRepository.deleteById(id);
    }

    public ProductsDto getProduct(Long id) {
        return productsManualMapper.toDto(productsRepository.findById(id).orElse(null));
    }

    public List<ProductsDto> getProductsByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (!productsRepository.findByCategory(category).isPresent()) {
            throw new RuntimeException("Category not found");
        }

        return switch (category) {
            case ELECTRONICS, CLOTHING, BOOKS, TOYS, FURNITURE ->
                    productsManualMapper.toDtoList(List.of(productsRepository.findByCategory(category).orElseThrow(() -> new RuntimeException())));
        };
    }
}
