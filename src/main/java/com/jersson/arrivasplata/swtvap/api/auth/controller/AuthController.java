package com.jersson.arrivasplata.swtvap.api.auth.controller;

import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthRefreshTokenRequest;
import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthRegisterRequest;
import com.jersson.arrivasplata.swtvap.api.auth.entity.AuthRequest;
import com.jersson.arrivasplata.swtvap.api.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class  AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<?>> index(Principal principal) {
        return Mono.just(ResponseEntity.ok()
                .body(principal == null ? "Hello anonymous" : principal.getName()));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest) {
        log.info("Welcome al login " + authService.authLogin(authRequest));
        return Mono.just(ResponseEntity.ok()
                .body(authService.authLogin(authRequest)));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<String>> register(@RequestBody AuthRegisterRequest authRequest) {

        return authService.authRegister(authRequest);
    }


    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<?>> refreshToken(@RequestBody AuthRefreshTokenRequest request) {

        return Mono.just(ResponseEntity.ok()
                .body(authService.authRefreshToken(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")//forbidden - prohibido
    @GetMapping("/read-secret")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<?>> readAdminSecret(Principal principal) {
        return Mono.just(ResponseEntity.ok()
                .body("Admin  - Hello " + principal.getName()));
    }

    @PreAuthorize("hasRole('USER')")//forbidden - prohibido
    @GetMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<?>> readUserSecret(Principal principal) {
        return Mono.just(ResponseEntity.ok()
                .body("User  - Hello " + principal.getName()));
    }

}
