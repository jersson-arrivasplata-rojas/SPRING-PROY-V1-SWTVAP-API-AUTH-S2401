package com.jersson.arrivasplata.swtvap.api.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

//https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;


    private static final String[] SECURITY_MATCHER_LIST = {
            "/api/v1/auth/**"
    };

    private static final String[] WHITE_LIST = {
            "/api/v1/auth",
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh-token"
    };
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers(SECURITY_MATCHER_LIST))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(WHITE_LIST).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer()
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new ReactiveJwtAuthenticationConverter()));

        //.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
    }
}