package com.jersson.arrivasplata.swtvap.api.auth.oauth;

import lombok.Data;
import lombok.Getter;
import org.springframework.core.env.Environment;

@Data
@Getter
public class OAuth2ServiceProperties {

    private String clientId;

    private String clientSecret;

    private String authorizationGrantType;

    private String redirectUri;

    private String scope;

    private String clientName;

    private String issuerUri;

    private String authorizationUri;

    private String tokenUri;

    private String jwtSetUri;

    private String userInfoUri;


    public OAuth2ServiceProperties(Environment env) {
        this.clientId = env.getProperty("spring.security.oauth2.client.registration.gateway.client-id");
        this.clientSecret = env.getProperty("spring.security.oauth2.client.registration.gateway.client-secret");
        this.authorizationGrantType = env.getProperty("spring.security.oauth2.client.registration.gateway.authorization-grant-type");
        this.redirectUri = env.getProperty("spring.security.oauth2.client.registration.gateway.redirect-uri");
        this.scope = env.getProperty("spring.security.oauth2.client.registration.gateway.scope");
        this.clientName = env.getProperty("spring.security.oauth2.client.registration.keycloak.client-name");
        this.issuerUri = env.getProperty("spring.security.oauth2.client.provider.keycloak.issuer-uri");
        this.authorizationUri = env.getProperty("spring.security.oauth2.client.provider.keycloak.authorization-uri");
        this.tokenUri = env.getProperty("spring.security.oauth2.client.provider.keycloak.token-uri");
        this.jwtSetUri = env.getProperty("spring.security.oauth2.client.provider.keycloak.jwk-set-uri");
        this.userInfoUri = env.getProperty("spring.security.oauth2.client.provider.keycloak.user-info-uri");
    }
}