package yers.dev.gateaway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Глобальный фильтр для Spring Cloud Gateway, ответственный за
 * управление и логирование идентификатора корреляции (correlation-id)
 * в каждом входящем HTTP-запросе.
 * <p>
 * Если в заголовках запроса уже присутствует {@link FilterUtility#CORRELATION_ID},
 * то он логируется. Иначе генерируется новый UUID и добавляется в заголовки.
 */
@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    /**
     * Основной метод фильтра. Проверяет наличие заголовка корреляции
     * и либо логирует существующий идентификатор, либо генерирует и устанавливает новый.
     *
     * @param exchange текущее состояние сервера (запрос/ответ)
     * @param chain    цепочка следующих фильтров Gateway
     * @return реактивный тип {@link Mono<Void>} для продолжения обработки запроса
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) {
            String existingId = filterUtility.getCorrelationId(requestHeaders);
            logger.debug("e-commerce-correlation-id found in RequestTraceFilter: {}", existingId);
        } else {
            String newId = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, newId);
            logger.debug("e-commerce-correlation-id generated in RequestTraceFilter: {}", newId);
        }
        return chain.filter(exchange);
    }

    /**
     * Проверяет, присутствует ли в заголовках запроса идентификатор корреляции.
     *
     * @param requestHeaders HTTP-заголовки запроса
     * @return {@code true}, если заголовок {@link FilterUtility#CORRELATION_ID} задан и не пустой
     */
    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        return filterUtility.getCorrelationId(requestHeaders) != null;
    }

    /**
     * Генерирует новый уникальный идентификатор корреляции на основе UUID.
     *
     * @return строковое представление сгенерированного UUID
     */
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
