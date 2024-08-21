package org.example.jwtauth.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

    private String username;

    private String  password;

    private String  email;

}
