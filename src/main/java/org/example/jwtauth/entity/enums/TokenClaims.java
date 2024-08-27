package org.example.jwtauth.entity.enums;

import lombok.Getter;

@Getter
public enum TokenClaims {

    JWT_ID("ID"),
    ISSUED_AT("issuedAt"),
    AUDIENCE("audience"),
    EXPIRATION("expiration_date"),
    USER_ID("user_id"),
    USER_NAME("user_name"),
    USER_EMAIL("user_email"),
    USER_ROLE("user_role"),
    USER_STATUS("user_status"),
    EXPIRES_AT("expired_at"),
    ISSUER("iss"),
    SUBJECT("subject")
    ;


    private final  String  value;

    TokenClaims(String value) {
        this.value = value;
    }
}
