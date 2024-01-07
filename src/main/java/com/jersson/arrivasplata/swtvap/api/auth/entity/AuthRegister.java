package com.jersson.arrivasplata.swtvap.api.auth.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegister {
    public String username;

    public String firstName;

    public String lastName;

    public String email;

    public Boolean enabled;

    public Credentials[] credentials;
}
