package yers.dev.gateaway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Конвертер, который извлекает роли из JWT, выданного Keycloak,
 * и преобразует их в коллекцию {@link GrantedAuthority} для Spring Security.
 * <p>
 * Ожидает, что в claim с именем "realm_access" хранится мапа,
 * содержащая список ролей под ключом "roles".
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Извлекает из переданного JWT информацию о ролях пользователя
     * (из claim "realm_access") и преобразует каждую роль в
     * экземпляр {@link SimpleGrantedAuthority} с префиксом "ROLE_".
     *
     * @param source JWT-токен, полученный от Keycloak
     * @return коллекция объектов {@link GrantedAuthority};
     *         пустая коллекция, если claim "realm_access" отсутствует или не содержит ролей
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }

        return roles.stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
