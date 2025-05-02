package yers.dev.gateaway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class GateawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateawayApplication.class, args);
    }

    @Bean
    public RouteLocator RouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/e-commerce/api/accounts/**", "/e-commerce/api/accounts")
                        .filters(f -> f.rewritePath("/e-commerce/api/accounts(?<segment>/?.*)", "/api/accounts${segment}")
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker"))
                        )
                        .uri("lb://ACCOUNTS")
                )
                .route(p -> p
                        .path("/e-commerce/api/products/**", "/e-commerce/api/products")
                        .filters(f -> f.rewritePath("/e-commerce/api/products(?<segment>/?.*)", "/api/products${segment}")
                                // путь остается неизменным, никакого rewritePath
                                .circuitBreaker(config -> config.setName("productsCircuitBreaker"))
                        )
                        .uri("lb://PRODUCTS"))
                .route(p -> p
                        .path("/e-commerce/auth/**")
                        .filters(f -> f.rewritePath("/e-commerce/auth/(?<segment>.*)", "/auth/${segment}")
                                .circuitBreaker(config -> config.setName("authCircuitBreaker")))
                        .uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path("/e-commerce/api/inventory/**", "/e-commerce/api/inventory")
                        .filters(f -> f.rewritePath("/e-commerce/api/inventory(?<segment>/?.*)", "/api/inventory${segment}")
                                .circuitBreaker(config -> config.setName("ordersCircuitBreaker")))
                        .uri("lb://INVENTORY"))
                .build();
    }


    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(10))
                        .build()).build());
    }
//
//    @Bean
//    public RedisRateLimiter redisRateLimiter() {
//        return new RedisRateLimiter(1, 1, 1);
//    }
//
//    @Bean
//    KeyResolver userKeyResolver() {
//        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
//                .defaultIfEmpty("anonymous");
//    }

}
