package yers.dev.account.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

/**
 * Утилитный компонент для взаимодействия с Keycloak через WebClient.
 * Предоставляет методы для отправки HTTP-запросов с form-data и JSON,
 * а также методы для создания, обновления и удаления ресурсов в Keycloak.
 */
@Component
@RequiredArgsConstructor
public class KeycloakHttpUtil {

    private final WebClient.Builder webClientBuilder;

    /**
     * Выполняет HTTP POST-запрос с данными формы (application/x-www-form-urlencoded)
     * и возвращает тело ответа в виде {@code Map<String, Object>}.
     *
     * @param baseUrl  базовый URL Keycloak (например,
     *                 {@code https://localhost:8080/realms/myrealm/protocol/openid-connect})
     * @param uri      относительный URI запроса (например, {@code "/token"})
     * @param formData данные формы (ключи и значения параметров, например grant_type, client_id и т.д.)
     * @return тело ответа от Keycloak, десериализованное в {@code Map<String, Object>}
     * @throws RuntimeException при ошибке соединения или разбора ответа
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
     * Выполняет HTTP POST-запрос с данными формы и не возвращает тело ответа.
     * Используется, когда достаточно лишь статуса выполнения операции.
     *
     * @param baseUrl  базовый URL Keycloak
     * @param uri      относительный URI запроса
     * @param formData данные формы для отправки
     * @throws RuntimeException при ошибке соединения
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
     * Выполняет HTTP POST-запрос с JSON-телом и заголовком Authorization Bearer.
     * При получении кода ответа вне диапазона 2xx выбрасывается {@link RuntimeException}
     * с телом ошибки.
     *
     * @param baseUrl базовый URL Keycloak
     * @param uri     относительный URI (например, {@code "/users"})
     * @param token   токен доступа в формате Bearer
     * @param body    объект, сериализуемый в JSON в теле запроса
     * @throws RuntimeException при получении неуспешного HTTP-статуса
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
     * Выполняет HTTP PUT-запрос с JSON-телом и заголовком Authorization Bearer.
     * При получении кода ответа вне диапазона 2xx выбрасывается {@link RuntimeException}
     * с телом ошибки.
     *
     * @param baseUrl базовый URL Keycloak
     * @param uri     относительный URI запроса
     * @param token   токен доступа в формате Bearer
     * @param body    объект, сериализуемый в JSON в теле запроса
     * @throws RuntimeException при получении неуспешного HTTP-статуса
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

    /**
     * Выполняет HTTP DELETE-запрос с заголовком Authorization Bearer.
     * При получении кода ответа вне диапазона 2xx выбрасывается {@link RuntimeException}
     * с телом ошибки.
     *
     * @param baseUrl базовый URL Keycloak
     * @param uri     относительный URI запроса
     * @param token   токен доступа в формате Bearer
     * @throws RuntimeException при получении неуспешного HTTP-статуса
     */
    public void deleteJson(String baseUrl, String uri, String token) {
        webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build()
                .delete()
                .uri(uri)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResp ->
                        clientResp.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "Delete failed: " + errorBody)))
                )
                .toBodilessEntity()
                .block();
    }

    /**
     * Создаёт нового пользователя в Keycloak, отправляя POST-запрос на {@code /users}.
     * В случае успешного создания (HTTP 201) возвращает ID созданного пользователя,
     * извлечённый из заголовка {@code Location}.
     *
     * @param realmUrl базовый URL realm (например, {@code https://localhost:8080/realms/myrealm})
     * @param token    токен доступа в формате Bearer
     * @param payload  объект с данными нового пользователя, сериализуемый в JSON
     * @return ID созданного пользователя в Keycloak
     * @throws RuntimeException при получении статуса отличного от HTTP 201 или ошибке ответа
     */
    public String createUser(String realmUrl, String token, Object payload) {
        var response = webClientBuilder
                .baseUrl(realmUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build()
                .post()
                .uri("/users")
                .bodyValue(payload)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.CREATED)) {
                        return clientResponse.toBodilessEntity();
                    } else {
                        return clientResponse
                                .bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "Keycloak create user failed: HTTP " +
                                                clientResponse.statusCode() + " / " + body
                                )));
                    }
                })
                .block();

        String location = response.getHeaders().getLocation().toString();
        return location.substring(location.lastIndexOf('/') + 1);
    }

}
