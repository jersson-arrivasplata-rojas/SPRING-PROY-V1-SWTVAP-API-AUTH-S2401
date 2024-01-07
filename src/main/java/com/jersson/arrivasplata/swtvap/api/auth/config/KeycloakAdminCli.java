package com.jersson.arrivasplata.swtvap.api.auth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KeycloakAdminCli {
    @Value("${keycloak.admin-server-url}")
    String adminServerUrl;
    @Value("${keycloak.manage-users.client-id}")
    String clientId;
    @Value("${keycloak.admin-cli.client-secret}")
    String clientSecret;
    @Value("${keycloak.admin-cli.grant_type}")
    String grantType;

}
