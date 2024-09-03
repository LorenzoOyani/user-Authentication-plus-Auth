package org.example.jwtauth.globalErrorHandler;

import lombok.Getter;

@Getter
public enum CustomErrorResponse {

    USER_ALREADY_EXISTS("INTERNAL_SERVER_ERROR", "user already exist!");


    private final String error;


    private final String message;

    CustomErrorResponse(String values, String message) {
        this.error = values;
        this.message = message;
    }
}
