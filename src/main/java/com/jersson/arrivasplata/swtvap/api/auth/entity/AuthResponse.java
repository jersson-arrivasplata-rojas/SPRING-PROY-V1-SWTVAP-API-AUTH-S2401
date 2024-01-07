package com.jersson.arrivasplata.swtvap.api.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("expires_in")
    public int expiresIn;
    @JsonProperty("refresh_expires_in")
    public int refreshExpiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("id_token")
    public String tokenId;
    @JsonProperty("not-before-policy")
    public int notBeforePolicy;
    @JsonProperty("session_state")
    public String sessionState;
    @JsonProperty("scope")
    public String scope;
}
