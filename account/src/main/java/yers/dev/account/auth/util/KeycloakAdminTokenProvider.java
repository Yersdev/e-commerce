package yers.dev.account.auth.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Утилитный компонент для получения access token администратора в Keycloak.
 * Использует Resource Owner Password Credentials Flow для получения токена.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakAdminTokenProvider {

    /**
     * WebClient для выполнения HTTP-запросов к Keycloak.
     */
    private final WebClient webClient = WebClient.create();

    /**
     * KeycloakHttpUtil util классов
     */
    private final KeycloakHttpUtil httpUtil;
    /**
     * Имя пользователя администратора, заданное в application.yml.
     */
    @Value("${keycloak.admin.username}")
    private String adminUsername;

    /**
     * Пароль администратора, заданный в application.yml.
     */
    @Value("${keycloak.admin.password}")
    private String adminPassword;

    /**
     * URL сервера Keycloak, например: http://localhost:8080/auth
     */
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    /**
     * Название Realm, в котором осуществляется аутентификация.
     */
    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Идентификатор клиента, настроенного в Keycloak для административного доступа.
     */
    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    /**
     * Секрет клиента, настроенного в Keycloak.
     */
    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    /**
     * Получает access token администратора из Keycloak.
     * Этот токен используется для выполнения административных операций
     * через REST API Keycloak, таких как регистрация или обновление пользователей.
     *
     * @return строка access_token
     */
    public String getAdminAccessToken() {
        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", adminClientId);
        form.add("client_secret", adminClientSecret);
        form.add("username", adminUsername);
        form.add("password", adminPassword);

        Map<String, Object> resp = httpUtil.postForm(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect",
                "/token",
                form
        );

        return (String) resp.get("access_token");
    }

}
