package org.example.jwtauth.globalErrorHandler;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class GlobalErrorResponse extends RuntimeException {

    private final CustomErrorResponse customErrorResponse;

    public GlobalErrorResponse(CustomErrorResponse customErrorResponse) {
        super(customErrorResponse.getError(), new Throwable(customErrorResponse.getMessage()));
        this.customErrorResponse = customErrorResponse;


    }


}