package org.example.jwtauth.exceptions;

import org.example.jwtauth.globalErrorHandler.CustomErrorResponse;

public class UserAlreadyExistError extends RuntimeException{

    UserAlreadyExistError(){
        super(CustomErrorResponse.USER_ALREADY_EXISTS.getMessage());
    }
}
