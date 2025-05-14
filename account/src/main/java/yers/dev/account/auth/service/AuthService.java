package yers.dev.account.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import yers.dev.account.auth.entity.dto.AuthRequest;
import yers.dev.account.auth.util.KeycloakHttpUtil;
import java.util.Map;

/**
 * Сервис для аутентификации пользователей через OpenID Connect с использованием Keycloak.
 * Предоставляет функционал входа, обновления токена и выхода из системы.
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    /**
     * Утилита для выполнения HTTP-запросов к Keycloak.
     */
    private final KeycloakHttpUtil httpUtil;

    /**
     * URL сервера авторизации Keycloak.
     */
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    /**
     * Название Realm в Keycloak.
     */
    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Идентификатор клиента, зарегистрированного в Keycloak.
     */
    @Value("${keycloak.client-id}")
    private String clientId;

    /**
     * Секрет клиента, зарегистрированного в Keycloak.
     */
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    /**
     * Выполняет вход пользователя с использованием grant_type=password.
     *
     * @param request объект запроса, содержащий email и пароль пользователя.
     * @return карта с токенами (access_token, refresh_token и др.).
     */
    public Map<String, Object> login(AuthRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("username", request.getEmail());
        form.add("password", request.getPassword());

        return httpUtil.postForm(keycloakBaseUrl(), "/token", form);
    }

    /**
     * Выполняет обновление access_token по refresh_token.
     *
     * @param refreshToken refresh_token пользователя.
     * @return новая пара токенов.
     */
    public Map<String, Object> refreshToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);

        return httpUtil.postForm(keycloakBaseUrl(), "/token", form);
    }

    /**
     * Выполняет logout пользователя (аннулирует refresh_token).
     *
     * @param refreshToken refresh_token, который необходимо отозвать.
     */
    public void logout(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("refresh_token", refreshToken);

        httpUtil.postVoid(keycloakBaseUrl(), "/logout", form);
    }

    /**
     * Строит базовый URL для запросов к OpenID Connect API Keycloak.
     *
     * @return базовый URL Keycloak OpenID.
     */
    private String keycloakBaseUrl() {
        return keycloakUrl + "/realms/" + realm + "/protocol/openid-connect";
    }
}
