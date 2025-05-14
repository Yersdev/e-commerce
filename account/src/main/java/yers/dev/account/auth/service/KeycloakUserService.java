package yers.dev.account.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import yers.dev.account.auth.entity.dto.RegistrationRequest;
import yers.dev.account.auth.util.KeycloakHttpUtil;
import yers.dev.account.auth.exception.FailedToDeleteKeycloak;
import yers.dev.account.account.mapper.AuthMapper;
import yers.dev.account.account.service.AccountsService;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления пользователями в Keycloak и локальной базе.
 * Обеспечивает регистрацию, обновление и получение сервисного токена.
 */
@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    /**
     * Утилита для HTTP-запросов к Keycloak.
     */
    private final KeycloakHttpUtil keycloakHttpUtil;


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
                "firstName",     req.getFirstName(),
                "lastName",      req.getLastName(),
                "email",         req.getEmail(),
                "emailVerified", true,
                "enabled",       true,
                "attributes", Map.of("phoneNumber", List.of(req.getPhoneNumber())),
                "credentials", List.of(Map.of(
                        "type",      "password",
                        "value",     req.getPassword(),
                        "temporary", false
                ))
        );

        String keycloakId = keycloakHttpUtil.createUser(
                keycloakUrl + "/admin/realms/" + realm,
                token,
                payload
        );

        accountsService.registerUser(req, keycloakId);

        return authService.login(AuthMapper.toAuthRequest(req));
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
                        "phoneNumber", List.of(req.getPhoneNumber())
                )
        );


        keycloakHttpUtil.putJson(
                keycloakUrl + "/admin/realms/" + realm,
                "/users/" + keycloakId,
                token,
                payload
        );

        accountsService.KeycloakUpdateAccount(req, keycloakId);
    }


    /**
     * Удаляет пользователя из Keycloak и локальной базы данных.
     * <p>
     * Алгоритм работы метода:
     * <ol>
     *   <li>Получает admin-токен для доступа к Keycloak.</li>
     *   <li>Отправляет DELETE-запрос к Keycloak Admin API для удаления пользователя по его internal ID.</li>
     *   <li>Логирует результат операции или ошибку от Keycloak.</li>
     *   <li>Удаляет соответствующую запись пользователя в локальном сервисе accountsService.</li>
     * </ol>
     *
     * @param keycloakId Internal ID пользователя в Keycloak
     * @throws RuntimeException если Keycloak вернёт ошибку при удалении
     */
    @Transactional
    public void deleteUser(String keycloakId) {
        String token = getAdminAccessToken();

        try {
            keycloakHttpUtil.deleteJson(
                    keycloakUrl + "/admin/realms/" + realm,
                    "/users/" + keycloakId,
                    token
            );
        } catch (WebClientResponseException e) {
            throw new FailedToDeleteKeycloak(e.getResponseBodyAsString());
        }

        accountsService.deleteUser(keycloakId);
    }

}