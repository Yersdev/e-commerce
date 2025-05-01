package yers.dev.account.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Утилитный компонент для взаимодействия с Keycloak через WebClient.
 * Предоставляет методы для отправки форм и JSON-запросов.
 */
@Component
@RequiredArgsConstructor
public class KeycloakHttpUtil {

    private final WebClient.Builder webClientBuilder;

    /**
     * Выполняет POST-запрос с form-data и возвращает тело ответа в виде Map.
     *
     * @param baseUrl базовый URL (например, https://localhost:8080/realms/myrealm/protocol/openid-connect)
     * @param uri     относительный URI (например, "/token")
     * @param formData данные формы (grant_type, client_id и т.д.)
     * @return тело ответа как Map<String, Object>
     */
    public Map<String, Object> postForm(String baseUrl, String uri, MultiValueMap<String, String> formData) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .post()
                .uri(uri)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    /**
     * Выполняет POST-запрос с form-data без обработки тела ответа.
     *
     * @param baseUrl базовый URL
     * @param uri     относительный путь запроса
     * @param formData данные формы
     */
    public void postVoid(String baseUrl, String uri, MultiValueMap<String, String> formData) {
        webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .post()
                .uri(uri)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    /**
     * Выполняет POST-запрос с JSON-телом и Bearer-токеном авторизации.
     * Если ответ не успешный (не 2xx), бросает исключение.
     *
     * @param baseUrl базовый URL
     * @param uri     относительный URI
     * @param token   Bearer-токен
     * @param body    тело запроса
     */
    public void postJson(String baseUrl, String uri, String token, Object body) {
        webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build()
                .post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResp ->
                        clientResp.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Post failed: " + errorBody)))
                )
                .toBodilessEntity()
                .block();
    }

    /**
     * Выполняет PUT-запрос с JSON-телом и Bearer-токеном авторизации.
     * Если ответ не успешный (не 2xx), бросает исключение.
     *
     * @param baseUrl базовый URL
     * @param uri     относительный URI
     * @param token   Bearer-токен
     * @param body    тело запроса
     */
    public void putJson(String baseUrl, String uri, String token, Object body) {
        webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build()
                .put()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResp ->
                        clientResp.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Put failed: " + errorBody)))
                )
                .toBodilessEntity()
                .block();
    }
}
