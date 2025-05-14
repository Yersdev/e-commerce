package yers.dev.gateaway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

/**
 * Конфигурационный класс, предоставляющий глобальный фильтр,
 * который добавляет идентификатор корреляции (correlation ID)
 * в заголовки ответа после обработки запроса.
 */
@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    /**
     * Создаёт и регистрирует глобальный фильтр, выполняющийся
     * после основного конвейера фильтров Gateway.
     * <p>
     * Фильтр:
     * <ul>
     *   <li>ждёт завершения обработки запроса;</li>
     *   <li>извлекает ранее установленный идентификатор корреляции из заголовков запроса;</li>
     *   <li>если в заголовках ответа ещё нет этого идентификатора, добавляет его;</li>
     *   <li>логирует факт добавления.</li>
     * </ul>
     *
     * @return экземпляр {@link GlobalFilter} для добавления correlation-id в ответ
     */
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) ->
                chain.filter(exchange)
                        .then(Mono.fromRunnable(() -> {
                            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                            String correlationId = filterUtility.getCorrelationId(requestHeaders);

                            if (!exchange.getResponse().getHeaders().containsKey(FilterUtility.CORRELATION_ID)) {
                                logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                                exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
                            }
                        }));
    }
}
