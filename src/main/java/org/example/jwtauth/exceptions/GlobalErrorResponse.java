package org.example.jwtauth.exceptions;

import lombok.Getter;
import org.example.jwtauth.globalErrorHandler.CustomErrorResponse;

@Getter
public class GlobalErrorResponse extends RuntimeException {

    private final CustomErrorResponse customErrorResponse;

    public GlobalErrorResponse(CustomErrorResponse customErrorResponse) {
        super(customErrorResponse.getError(), new Throwable(customErrorResponse.getMessage()));
        this.customErrorResponse = customErrorResponse;


    }


}