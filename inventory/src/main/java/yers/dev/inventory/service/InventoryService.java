package yers.dev.inventory.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import yers.dev.inventory.dto.InventoryDto;
import yers.dev.inventory.dto.ProductInventoryDto;
import yers.dev.inventory.dto.ProductsDto;
import yers.dev.inventory.entity.Inventory;
import yers.dev.inventory.exception.ProductNotFoundException;
import yers.dev.inventory.mapper.InventoryMapper;
import yers.dev.inventory.repository.InventoryRepository;
import yers.dev.inventory.service.client.ProductFeignClient;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryService {

    private final ProductFeignClient productFeignClient;
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public ProductInventoryDto fetchProduct(Long productId) {
        InventoryDto inventoryDto = inventoryMapper.toInventoryDto(inventoryRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString())));
        ResponseEntity<ProductsDto> productsDtoResponseEntity = productFeignClient.fetchProductDetails("correlationId",productId);
        ProductInventoryDto productInventoryDto = new ProductInventoryDto();
        productInventoryDto.setProduct_inventory_id(inventoryDto.getInventory_id());
        productInventoryDto.setName(productsDtoResponseEntity.getBody().getName());
        productInventoryDto.setDescription(productsDtoResponseEntity.getBody().getDescription());
        productInventoryDto.setPrice(productsDtoResponseEntity.getBody().getPrice());
        productInventoryDto.setStock_quantity(inventoryDto.getQuantity());
        productInventoryDto.setCategory(productsDtoResponseEntity.getBody().getCategory());
        productInventoryDto.setQuantity(inventoryDto.getQuantity());
        return productInventoryDto;

    }


    public List<InventoryDto> fetchAllProducts() {
        System.out.println("fetching all products{} " + inventoryRepository.findAll());
        return inventoryMapper.toInventoryDto(inventoryRepository.findAll());
    }

    @Transactional
    public void addInventory(@Valid InventoryDto inventoryDto) {
        inventoryRepository.save(inventoryMapper.toInventory(inventoryDto));
    }

    public void updateInventory(InventoryDto inventoryDto) {
        inventoryRepository.save(inventoryMapper.toInventory(inventoryDto));
    }
}
