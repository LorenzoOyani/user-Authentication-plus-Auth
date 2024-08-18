package org.example.jwtauth.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {

    private String accessToken;
}
