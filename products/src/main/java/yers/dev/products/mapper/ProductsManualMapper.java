package yers.dev.products.mapper;

import org.springframework.stereotype.Component;
import yers.dev.products.model.dto.ProductsDto;
import yers.dev.products.model.Products;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductsManualMapper {

    public ProductsDto toDto(Products product) {
        if (product == null) return null;
        ProductsDto dto = new ProductsDto();
        dto.setProduct_id(product.getProduct_id());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        return dto;
    }

    public Products toEntity(ProductsDto dto) {
        if (dto == null) return null;
        Products product = new Products();
        product.setProduct_id(dto.getProduct_id());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        // product.setIsActive(...) — при необходимости проставьте самостоятельно
        product.setCategory(dto.getCategory());
        return product;
    }

    public List<ProductsDto> toDtoList(List<Products> products) {
        return products == null
                ? null
                : products.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Products> toEntityList(List<ProductsDto> dtos) {
        return dtos == null
                ? null
                : dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
