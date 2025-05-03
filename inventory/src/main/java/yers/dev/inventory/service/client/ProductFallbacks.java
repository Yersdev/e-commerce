package yers.dev.inventory.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import yers.dev.inventory.entity.dto.ProductsDto;

@Component
public class ProductFallbacks implements ProductFeignClient{


    @Override
    public ResponseEntity<ProductsDto> fetchProductDetails(String correlationId, Long id) {
        return null;
    }
}
