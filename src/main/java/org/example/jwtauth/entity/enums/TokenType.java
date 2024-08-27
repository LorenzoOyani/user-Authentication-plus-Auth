package org.example.jwtauth.entity.enums;

public enum TokenType {

    BEARER("Bearer");

    private String value;

    TokenType(String bearer) {
        this.value = bearer;
    }
}
