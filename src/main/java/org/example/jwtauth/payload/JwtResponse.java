package org.example.jwtauth.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.example.jwtauth.entity.enums.TokenType;

@Builder
public class JwtResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    private TokenType tokenType;



    private String refreshToken;

    private String email;
}
