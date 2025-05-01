package yers.dev.inventory.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import yers.dev.inventory.dto.ProductsDto;

@FeignClient(name = "product", fallback = ProductFallbacks.class)
public interface ProductFeignClient {
    @GetMapping(value = "/api/products/fetch/{id}", consumes = "application/json")
    ResponseEntity<ProductsDto> fetchProductDetails(@RequestHeader("correlationId") String correlationId, @PathVariable("id") Long id);
}