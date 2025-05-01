package yers.dev.account.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация {@link WebClient} для взаимодействия с сервером Keycloak.
 * <p>
 * Устанавливает базовый URL и заголовок {@code Content-Type: application/x-www-form-urlencoded}
 * для всех запросов к Keycloak.
 */
@Configuration
public class WebClientConfig {

    /**
     * Создаёт и настраивает {@link WebClient.Builder} для взаимодействия с Keycloak.
     *
     * @param props конфигурационные свойства Keycloak (URL, realm и т.д.)
     * @return сконфигурированный {@link WebClient.Builder}
     */
    @Bean
    public WebClient.Builder keycloakWebClientBuilder(KeycloakProperties props) {
        return WebClient.builder()
                .baseUrl(props.getAuthServerUrl() + "/realms/" + props.getRealm())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }
}
