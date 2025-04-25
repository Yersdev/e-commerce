package yers.dev.products.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yers.dev.products.dto.ProductsDto;
import yers.dev.products.service.ProductsService;

import java.util.List;

@Controller
@RequestMapping("/api/products")
@AllArgsConstructor
@RestController
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping
    public List<ProductsDto> getProducts() {
        return productsService.getProducts();
    }


}
