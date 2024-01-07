package com.jersson.arrivasplata.swtvap.api.auth.service;

import com.jersson.arrivasplata.swtvap.api.auth.config.KeycloakAdminCli;
import com.jersson.arrivasplata.swtvap.api.auth.entity.*;
import com.jersson.arrivasplata.swtvap.api.auth.oauth.OAuth2ServiceProperties;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Service
public class AuthService implements IAuthService {

    @Autowired
    Environment env;

    @Autowired
    RestTemplate restTemplate;

    KeycloakAdminCli keycloakAdminCli;

    public AuthService(KeycloakAdminCli KeycloakAdminCli) {
        this.keycloakAdminCli = KeycloakAdminCli;
    }

    @Override
    public AuthResponse authLogin(AuthRequest userRequest) {

        OAuth2ServiceProperties oAuth2ServiceProperties = new OAuth2ServiceProperties(env);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", userRequest.getUsername());
        map.add("password", userRequest.getPassword());
        map.add("client_id", oAuth2ServiceProperties.getClientId());
        map.add("grant_type", oAuth2ServiceProperties.getAuthorizationGrantType());
        map.add("client_secret", oAuth2ServiceProperties.getClientSecret());
        map.add("scope", oAuth2ServiceProperties.getScope());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(oAuth2ServiceProperties.getTokenUri(), request, AuthResponse.class);
    }

    @Override
    public Mono<ResponseEntity<String>> authRegister(AuthRegisterRequest userRequest) {

        AuthResponse authResponseAdminCli = authTokenAdminCli();

        AuthRegister authRegister = new AuthRegister();
        authRegister.setUsername(userRequest.getUsername());
        authRegister.setFirstName(userRequest.getFirstName());
        authRegister.setLastName(userRequest.getLastName());
        authRegister.setEmail(userRequest.getEmail());
        authRegister.setEnabled(true);

        Credentials[] credentials = new Credentials[]{
            new Credentials(CredentialRepresentation.PASSWORD, userRequest.getPassword(), false)
        };
        authRegister.setCredentials(credentials);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authResponseAdminCli.tokenType + " " + authResponseAdminCli.accessToken);
        headers.add("Content-Type", "application/json");
        String response = null;
        int code = 200;
        try {
            HttpEntity<AuthRegister> request = new HttpEntity<>(authRegister, headers);
            response = restTemplate.postForObject(keycloakAdminCli.getAdminServerUrl() + "/users", request, String.class);
        } catch (HttpClientErrorException e) {
            response = e.getResponseBodyAs(String.class);
            code = e.getStatusCode().value();
        } finally {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            return Mono.just(ResponseEntity.status(code).headers(httpHeaders).body(response));
        }
    }


    @Override
    public AuthResponse authRefreshToken(AuthRefreshTokenRequest tokenRequest) {

        OAuth2ServiceProperties oAuth2ServiceProperties = new OAuth2ServiceProperties(env);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", oAuth2ServiceProperties.getClientId());
        map.add("client_secret", oAuth2ServiceProperties.getClientSecret());
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", tokenRequest.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(oAuth2ServiceProperties.getTokenUri(), request, AuthResponse.class);
    }

    public AuthResponse authTokenAdminCli() {

        OAuth2ServiceProperties oAuth2ServiceProperties = new OAuth2ServiceProperties(env);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", keycloakAdminCli.getGrantType());
        map.add("client_id", keycloakAdminCli.getClientId());
        map.add("client_secret", keycloakAdminCli.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(oAuth2ServiceProperties.getTokenUri(), request, AuthResponse.class);
    }


}
