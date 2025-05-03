package yers.dev.inventory.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import yers.dev.inventory.entity.dto.InventoryDto;
import yers.dev.inventory.entity.dto.ProductInventoryDto;
import yers.dev.inventory.entity.dto.ProductsDto;
import yers.dev.inventory.entity.Inventory;
import yers.dev.inventory.exception.ProductNotFoundException;
import yers.dev.inventory.mapper.InventoryMapper;
import yers.dev.inventory.repository.InventoryRepository;
import yers.dev.inventory.service.client.ProductFeignClient;
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
        return productInventoryDto;

    }


    public List<InventoryDto> fetchAllProducts() {
        return inventoryMapper.toInventoryDto(inventoryRepository.findAll());
    }

    @Transactional
    public void addInventory(@Valid InventoryDto inventoryDto) {
        inventoryRepository.save(inventoryMapper.toInventory(inventoryDto));
    }

    @Transactional
    public void updateInventory(Long productId, InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        inventory.setQuantity(inventoryDto.getQuantity());
        inventory.setWarehouseLocation(inventoryDto.getWarehouseLocation());
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(Long productId) {
        inventoryRepository.deleteById(productId);
    }
}
