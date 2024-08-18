package org.example.jwtauth.entity.enums;

import lombok.Getter;

@Getter
public enum TokenClaims {

    JTI("id"),
    ISSUED_AT("issuedAt"),
    AUDIENCE("audience"),
    EXPIRATION("expiration_date"),
    ISSUER("iss"),
    SUBJECT("subject")
    ;


    private final  String  value;

    TokenClaims(String value) {
        this.value = value;
    }
}
