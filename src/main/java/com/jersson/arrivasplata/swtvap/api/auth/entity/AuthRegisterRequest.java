package com.jersson.arrivasplata.swtvap.api.auth.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class AuthRegisterRequest {
    String username;
    String firstName;
    String lastName;
    String email;
    String password;
}
