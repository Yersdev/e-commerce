package yers.dev.account.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.auth.util.KeycloakHttpUtil;
import yers.dev.account.mapper.AuthMapper;
import yers.dev.account.service.AccountsService;

import java.util.List;
import java.util.Map;
/**
 * Сервис для управления пользователями в Keycloak и локальной базе.
 * Обеспечивает регистрацию, обновление и получение сервисного токена.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserService {

    /**
     * Утилита для HTTP-запросов к Keycloak.
     */
    private final KeycloakHttpUtil keycloakHttpUtil;

    private final WebClient.Builder webClientBuilder;


    /**
     * Сервис для управления локальными пользователями.
     */
    private final AccountsService accountsService;

    /**
     * Сервис аутентификации.
     */
    private final AuthService authService;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    /**
     * Получает сервисный access_token от Keycloak по client_credentials.
     *
     * @return access_token в виде строки
     */
    public String getAdminAccessToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", adminClientId);
        form.add("client_secret", adminClientSecret);

        return (String) keycloakHttpUtil.postForm(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect",
                "/token",
                form
        ).get("access_token");
    }

    /**
     * Регистрирует пользователя в Keycloak и локальной базе.
     * <p>
     * Использует admin-токен для создания пользователя в Keycloak,
     * затем вызывает локальный {@code UsersService} для создания пользователя.
     *
     * @param req данные пользователя для регистрации
     * @return объект с токенами после успешной авторизации
     */
    @Transactional
    public Map<String, Object> registerUser(RegistrationRequest req) {
        String token = getAdminAccessToken();
        Map<String, Object> payload = Map.of(
                "firstName", req.getFirstName(),
                "lastName", req.getLastName(),
                "email", req.getEmail(),
                "emailVerified", true,
                "enabled", true,
                "attributes", Map.of(
                        "phoneNumber", List.of(req.getPhoneNumber())  // обязательно как List
                ),
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", req.getPassword(),
                        "temporary", false
                ))
        );

        try {
            // вместо retrieve() используем exchangeToMono, чтобы точно обработать статус и тело
            var response = webClientBuilder
                    .baseUrl(keycloakUrl + "/admin/realms/" + realm)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .post().uri("/users")
                    .bodyValue(payload)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().equals(HttpStatus.CREATED)) {
                            return clientResponse.toBodilessEntity();
                        } else {
                            return clientResponse
                                    .bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Create user failed: HTTP " +
                                                    clientResponse.statusCode() +
                                                    " / body: " + body
                                    )));
                        }
                    })
                    .block();

            // здесь можно извлечь Location и keycloakId если нужно
            String location = response.getHeaders().getLocation().toString();
            String keycloakId = location.substring(location.lastIndexOf('/') + 1);

            accountsService.registerUser(req, keycloakId);


        } catch (WebClientResponseException e) {
            // явная логика логирования, чтобы увидеть тело ошибки
            log.error("Keycloak returned {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;  // или бросить своё исключение с более понятным сообщением
        }

        Map<String,Object> resp = authService.login(AuthMapper.toAuthRequest(req));
        return resp;
    }

    /**
     * Обновляет данные пользователя в Keycloak и локальной базе.
     *
     * @param keycloakId UUID пользователя в Keycloak
     * @param req обновлённые данные пользователя
     */
    @Transactional
    public void updateUser(String keycloakId, RegistrationRequest req) {
        String token = getAdminAccessToken();

        Map<String, Object> payload = Map.of(
                "firstName", req.getFirstName(),
                "lastName", req.getLastName(),
                "email", req.getEmail(),
                "emailVerified", true,
                "enabled", true,
                "attributes", Map.of(
                        "phoneNumber", List.of(req.getPhoneNumber()) // важно: List
                )
        );


        keycloakHttpUtil.putJson(
                keycloakUrl + "/admin/realms/" + realm,
                "/users/" + keycloakId,
                token,
                payload
        );

        accountsService.updateAccount(req, keycloakId);
    }
}