package yers.dev.gateaway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GateawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateawayApplication.class, args);
    }

    @Bean
    public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/api/accounts/**")
                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/e-commerce/api/accounts${segment}")
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker"))
                        ).uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path("/api/products/**")
                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/e-commerce/api/products/${segment}")
                                .circuitBreaker(config -> config.setName("productsCircuitBreaker")))
                        .uri("lb://PRODUCTS"))
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
