package org.example.jwtauth.globalErrorHandler;

public class UserAlreadyExistError extends RuntimeException{

    UserAlreadyExistError(){
        super(CustomErrorResponse.USER_ALREADY_EXISTS.getMessage());
    }
}
