//package yers.dev.gateaway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import reactor.core.publisher.Mono;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        serverHttpSecurity
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchanges -> exchanges
//
//                        .pathMatchers("/api/auth/**").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
//                        .pathMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
//
//                        .pathMatchers("/api/users/me", "/api/users/me/**").authenticated()
//
//                        .pathMatchers("/api/users/**").hasRole("ADMIN")
//
//                        .pathMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("ADMIN")
//
//                        .pathMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.PATCH, "/api/categories/**").hasRole("ADMIN")
//
//                        // Everything else
//                        .anyExchange().authenticated())
//                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
//                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
//        return serverHttpSecurity.build();
//    }
//
//    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
//        JwtAuthenticationConverter jwtAuthenticationConverter =
//                new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
//                (new KeycloakRoleConverter());
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }
//}