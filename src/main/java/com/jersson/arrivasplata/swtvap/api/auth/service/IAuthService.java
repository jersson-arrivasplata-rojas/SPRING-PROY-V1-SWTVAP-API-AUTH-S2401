package com.jersson.arrivasplata.swtvap.api.auth.service;

import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthRefreshTokenRequest;
import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthRegisterRequest;
import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthResponse;
import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IAuthService {

    AuthResponse authLogin(AuthRequest userRequest);
    Mono<ResponseEntity<String>> authRegister(AuthRegisterRequest userRequest);

    AuthResponse authRefreshToken(AuthRefreshTokenRequest tokenRequest);

    Object authLogout(String postLogoutRedirectUri, String idTokenHint);
}
