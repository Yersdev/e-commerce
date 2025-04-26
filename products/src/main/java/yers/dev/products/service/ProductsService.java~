package yers.dev.products.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import yers.dev.products.dto.ProductsDto;
import yers.dev.products.mapper.ProductsMapperImpl;
import yers.dev.products.model.Category;
import yers.dev.products.repository.ProductsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ProductsMapperImpl productsMapper;

    public List<ProductsDto> getProducts() {
        return productsMapper.toProductsDto(productsRepository.findAll());
    }

    @Transactional
    public ProductsDto addProduct(ProductsDto productsDto) {
        return productsMapper.toProductsDto(productsRepository.save(productsMapper.toProducts(productsDto)));
    }

    @Transactional
    public ProductsDto updateProduct(ProductsDto productsDto) {
        return productsMapper.toProductsDto(productsRepository.save(productsMapper.toProducts(productsDto)));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }

    public ProductsDto getProduct(Long id) {
        return productsMapper.toProductsDto(productsRepository.findById(id).orElse(null));
    }

    public List<ProductsDto> getByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }

        return switch (category) {
            case ELECTRONICS, CLOTHING, BOOKS, TOYS, FURNITURE ->
                    productsMapper.toProductsDto(List.of(productsRepository.findByCategory(category).orElseThrow(() -> new RuntimeException())));
        };
    }
}
