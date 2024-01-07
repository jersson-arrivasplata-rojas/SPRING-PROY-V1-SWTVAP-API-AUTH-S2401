package com.jersson.arrivasplata.swtvap.api.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class Keycloak {
    String clientId;
    String clientSecret;
    String grantType;

    public Keycloak(String name, String accountNumber, String grantType) {
        this.clientId = name;
        this.clientSecret = accountNumber;
        this.grantType = grantType;
    }
}