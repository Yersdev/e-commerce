package yers.dev.account.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Stream;

/**
 * Конфигурация Spring Security для интеграции с Keycloak.
 * <p>
 * Включает поддержку JWT и OIDC авторизации, а также обрабатывает роли из кастомного claim {@code spring_sec_roles}.
 */
@Configuration
public class KeycloakConfiguration {

    /**
     * Основной security filter chain.
     *
     * @param http конфигурация {@link HttpSecurity}
     * @return настроенный {@link SecurityFilterChain}
     * @throws Exception при ошибке конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF выключен для публичных и H2 эндпоинтов
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/h2-console/**",
                                "/auth/register",
                                "/auth/login",
                                "/auth/refresh",
                                "/auth/logout"
                        )
                )
                // 2. Разрешить фреймы (для H2 консоли)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()).disable()
                )
                // 3. Настройка JWT + OAuth2 входа
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(converter()))
                )
                .oauth2Login(Customizer.withDefaults())
                // 4. Доступы
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/auth/register",
                                "/auth/login",
                                "/auth/refresh",
                                "/auth/logout",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Конвертер для преобразования JWT токена в {@link org.springframework.security.core.Authentication}
     * с учетом пользовательского клейма {@code spring_sec_roles}.
     *
     * @return {@link JwtAuthenticationConverter} с кастомной логикой ролей
     */
    @Bean
    public JwtAuthenticationConverter converter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = jwt.getClaimAsStringList("spring_sec_roles");

            return Stream.concat(
                            authorities.stream(),
                            roles.stream()
                                    .filter(r -> r.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });
        return converter;
    }

    /**
     * Пользовательский {@link OAuth2UserService} для OIDC,
     * добавляющий роли из {@code spring_sec_roles} в {@link OidcUser}.
     *
     * @return модифицированный {@link OAuth2UserService}
     */
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        var oidcUserService = new OidcUserService();

        return userRequest -> {
            var oidcUser = oidcUserService.loadUser(userRequest);
            var roles = oidcUser.getClaimAsStringList("spring_sec_roles");
            var authorities = Stream.concat(
                            oidcUser.getAuthorities().stream(),
                            roles.stream()
                                    .filter(r -> r.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }
}
