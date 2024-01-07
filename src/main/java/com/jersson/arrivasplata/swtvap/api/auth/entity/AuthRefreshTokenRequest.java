package com.jersson.arrivasplata.swtvap.api.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRefreshTokenRequest {
    private String refreshToken;

}