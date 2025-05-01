package yers.dev.inventory.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import yers.dev.inventory.dto.InventoryDto;
import yers.dev.inventory.dto.ProductsDto;
import yers.dev.inventory.exception.ExternalServiceException;
import yers.dev.inventory.exception.ExternalServiceUnavailableException;
import yers.dev.inventory.exception.GeneralExternalCallException;
import yers.dev.inventory.exception.ProductNotFoundException;
import yers.dev.inventory.service.client.ProductFeignClient;

@Service
@AllArgsConstructor
public class InventoryService {

    private final ProductFeignClient productFeignClient;



    public ProductsDto fetchProduct(Long productId) {
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            return webClient.get()
                    .uri("/fetch/{id}", productId)   // подставляем {id} в адрес
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        return Mono.error(new ProductNotFoundException("Product with ID " + productId + " not found."));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        return Mono.error(new ExternalServiceException("Product Service returned 5xx error."));
                    })
                    .bodyToMono(ProductsDto.class)
                    .block(); // тут мы блокируем и превращаем в обычный синхронный вызов (как RestTemplate)
        } catch (WebClientRequestException e) {
            // Проблемы с сетью (например, сервис недоступен)
            throw new ExternalServiceUnavailableException("Product Service is unavailable: " + e.getMessage());
        } catch (Exception e) {
            // Ловим всё остальное
            throw new GeneralExternalCallException("Unexpected error occurred: " + e.getMessage());
        }
    }

    public ProductsDto createProduct(ProductsDto productDto) {
}

    public InventoryDto fetchAllProducts() {

    }
    }
