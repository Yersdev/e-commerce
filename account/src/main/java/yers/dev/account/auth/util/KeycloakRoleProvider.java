package yers.dev.account.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import yers.dev.account.auth.configuration.KeycloakProperties;
import yers.dev.account.auth.entity.UserRole;
import yers.dev.account.auth.entity.dto.KeycloakRoleResponseDto;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Утилитный компонент для получения и назначения ролей пользователям в Keycloak.
 * Использует WebClient и кеширует ID ролей для ускорения повторных вызовов.
 */
@Component
@RequiredArgsConstructor
public class KeycloakRoleProvider {

    private final WebClient keycloakWebClient = WebClient.create();
    private final KeycloakAdminTokenProvider adminTokenProvider;
    private final KeycloakProperties props;
    private final Map<String, String> roleCache = new ConcurrentHashMap<>();

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    private String keycloakUrl;

    /**
     * Получает DTO с информацией о роли по enum-значению {@link UserRole}.
     * Кеширует ID роли для последующего использования.
     *
     * @param role роль, которую нужно получить из Keycloak
     * @return DTO с ID и именем роли
     */
    public KeycloakRoleResponseDto getRole(UserRole role) {
        String name = role.name();
        if (roleCache.containsKey(name)) {
            return new KeycloakRoleResponseDto(roleCache.get(name), name);
        }

        String token = adminTokenProvider.getAdminAccessToken();
        KeycloakRoleResponseDto dto = keycloakWebClient.get()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token") // NOTE: URI appears incorrect
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(KeycloakRoleResponseDto.class)
                .block();

        roleCache.put(name, dto.getId());
        return dto;
    }

    /**
     * Назначает роль пользователю в Keycloak на уровне realm.
     *
     * @param roleDto объект роли (id + name)
     * @param userId  UUID пользователя в Keycloak
     */
    public void assignRole(KeycloakRoleResponseDto roleDto, String userId) {
        String token = adminTokenProvider.getAdminAccessToken();
        keycloakWebClient.post()
                .uri(uri -> uri
                        .path("/admin/realms/{realm}/users/{userId}/role-mappings/realm")
                        .build(props.getRealm(), userId))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(roleDto))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
