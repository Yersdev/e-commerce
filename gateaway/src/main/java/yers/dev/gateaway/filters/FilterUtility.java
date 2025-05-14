package yers.dev.gateaway.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * Утилитарный компонент для работы с идентификатором корреляции (correlation ID)
 * в HTTP-заголовках внутри Spring Cloud Gateway.
 */
@Component
public class FilterUtility {

    /**
     * Имя HTTP-заголовка для передачи идентификатора корреляции.
     */
    public static final String CORRELATION_ID = "e-commerce-correlation-id";

    /**
     * Извлекает идентификатор корреляции из HTTP-заголовков запроса.
     *
     * @param requestHeaders HTTP-заголовки запроса
     * @return значение заголовка {@value #CORRELATION_ID},
     *         либо {@code null}, если такого заголовка нет
     */
    public String getCorrelationId(HttpHeaders requestHeaders) {
        List<String> values = requestHeaders.get(CORRELATION_ID);
        return (values != null && !values.isEmpty())
                ? values.get(0)
                : null;
    }

    /**
     * Добавляет или обновляет HTTP-заголовок в {@link ServerWebExchange}.
     *
     * @param exchange исходный обмен HTTP-запроса/ответа
     * @param name     имя заголовка
     * @param value    значение заголовка
     * @return новый {@link ServerWebExchange} с добавленным или обновлённым заголовком
     */
    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .header(name, value)
                        .build())
                .build();
    }

    /**
     * Добавляет или обновляет заголовок идентификатора корреляции в {@link ServerWebExchange}.
     *
     * @param exchange      исходный обмен HTTP-запроса/ответа
     * @param correlationId значение идентификатора корреляции
     * @return новый {@link ServerWebExchange} с добавленным или обновлённым заголовком {@value #CORRELATION_ID}
     */
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}
