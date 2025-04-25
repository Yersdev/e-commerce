package yers.dev.products.mapper;

import org.mapstruct.Mapper;
import yers.dev.products.dto.ProductsDto;
import yers.dev.products.model.Products;

import java.util.List;

@Mapper
    public interface ProductsMapper {

        ProductsDto toProductsDto(Products products);
        Products toProducts(ProductsDto productsDto);

        List<ProductsDto> toProductsDto(List<Products> products);
        List<Products> toProducts(List<ProductsDto> productsDto);
    }