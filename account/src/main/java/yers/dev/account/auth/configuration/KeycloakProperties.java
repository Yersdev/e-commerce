package yers.dev.account.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для параметров подключения к Keycloak.
 * <p>
 * Загружается из application.properties или application.yml с префиксом {@code keycloak}.
 *
 * <p>Пример:
 * <pre>{@code
 * keycloak.auth-server-url=http://localhost:8080
 * keycloak.realm=myrealm
 * keycloak.client-id=myclient
 * keycloak.client-secret=mysecret
 * keycloak.admin-client-id=admin-cli
 * keycloak.admin-client-secret=admin-secret
 * }</pre>
 */
@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
public class KeycloakProperties {

    /** URL сервера авторизации Keycloak (например, http://localhost:8080) */
    private String authServerUrl;

    /** Имя используемого Realm в Keycloak */
    private String realm;

    /** ID клиента для пользовательской аутентификации */
    private String clientId;

    /** Секрет клиента для пользовательской аутентификации */
    private String clientSecret;

    /** ID администратора клиента (например, admin-cli) */
    private String adminClientId;

    /** Секрет администратора клиента */
    private String adminClientSecret;

}
