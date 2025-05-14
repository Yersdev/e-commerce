package yers.dev.gateaway.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Конфигурация Spring Security для WebFlux-приложения,
 * настроенного как ресурсный сервер OAuth2 с JWT.
 * <p>
 * Определяет правила авторизации HTTP-запросов и декодер JWT,
 * а также конвертор для извлечения ролей из токена.
 */
@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    /**
     * Настраивает цепочку безопасности для HTTP-запросов:
     * <ul>
     *     <li>Отключает CSRF-защиту.</li>
     *     <li>Разрешает публичный доступ к путям {@code auth/**} и {@code /e-commerce/auth/**}.</li>
     *     <li>Требует аутентификацию для всех остальных запросов.</li>
     *     <li>Использует JWT для ресурсного сервера и подключает конвертер ролей.</li>
     * </ul>
     *
     * @param http объект для настройки HTTP-безопасности
     * @return настроенный {@link SecurityWebFilterChain}
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("auth/**", "/e-commerce/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                );

        return http.build();
    }

    /**
     * Создаёт декодер JWT на основе URL-адреса издателя (Issuer).
     * Используется для валидации и разбора токена, полученного от Keycloak.
     * <p>
     * Пример URL: {@code http://localhost:8080/realms/yers__check}
     *
     * @return {@link ReactiveJwtDecoder} для реактивного декодирования JWT
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation("http://localhost:8080/realms/yers__check");
    }

    /**
     * Создаёт конвертер, который извлекает список ролей из JWT-клейма {@code spring_sec_roles},
     * отфильтровывает только те, что начинаются с префикса {@code ROLE_}, и преобразует
     * их в объекты {@link SimpleGrantedAuthority}.
     * <p>
     * Возвращает адаптер для реактивного стиля {@code Mono<AbstractAuthenticationToken>}.
     *
     * @return {@link Converter} из {@link Jwt} в {@link Mono<AbstractAuthenticationToken>}
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("spring_sec_roles");
            if (roles == null) {
                roles = List.of();
            }
            return roles.stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }
}
