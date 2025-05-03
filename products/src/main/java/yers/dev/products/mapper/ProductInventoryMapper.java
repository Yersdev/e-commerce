package yers.dev.products.mapper;

import yers.dev.products.dto.InventoryDto;
import yers.dev.products.dto.ProductInventoryDto;
import yers.dev.products.dto.ProductsDto;

public class ProductInventoryMapper {

    public static ProductsDto toProductsDto(ProductInventoryDto dto) {
        ProductsDto productsDto = new ProductsDto();
        productsDto.setName(dto.getName());
        productsDto.setDescription(dto.getDescription());
        productsDto.setPrice(dto.getPrice());
        productsDto.setCategory(dto.getCategory());
        return productsDto;
    }

    public static InventoryDto toInventoryDto(ProductInventoryDto dto) {
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setQuantity(dto.getStock_quantity());
        inventoryDto.setWarehouseLocation(dto.getWarehouseLocation());
        return inventoryDto;
    }
}
