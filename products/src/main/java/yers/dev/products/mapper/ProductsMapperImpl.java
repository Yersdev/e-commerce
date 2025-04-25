package yers.dev.products.mapper;

import org.springframework.stereotype.Component;
import yers.dev.products.dto.ProductsDto;
import yers.dev.products.model.Products;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsMapperImpl implements ProductsMapper {

    @Override
    public ProductsDto toProductsDto(Products products) {
        if (products == null) {
            return null;
        }

        ProductsDto dto = new ProductsDto();
        dto.setProduct_id(products.getProduct_id());
        dto.setName(products.getName());
        dto.setDescription(products.getDescription());
        dto.setPrice(products.getPrice());
        dto.setStock_quantity(products.getStock_quantity());
        dto.setCategory(products.getCategory());

        return dto;
    }

    @Override
    public Products toProducts(ProductsDto dto) {
        if (dto == null) {
            return null;
        }

        Products product = new Products();
        product.setProduct_id(dto.getProduct_id());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock_quantity(dto.getStock_quantity());
        product.setCategory(dto.getCategory());

        return product;
    }

    @Override
    public List<ProductsDto> toProductsDto(List<Products> productsList) {
        if (productsList == null) {
            return null;
        }

        List<ProductsDto> dtoList = new ArrayList<>();
        for (Products product : productsList) {
            dtoList.add(toProductsDto(product));
        }
        return dtoList;
    }

    @Override
    public List<Products> toProducts(List<ProductsDto> dtoList) {
        if (dtoList == null) {
            return null;
        }

        List<Products> productsList = new ArrayList<>();
        for (ProductsDto dto : dtoList) {
            productsList.add(toProducts(dto));
        }
        return productsList;
    }
}
