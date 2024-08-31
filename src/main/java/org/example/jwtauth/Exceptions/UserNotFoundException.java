package org.example.jwtauth.Exceptions;

public class UserNotFoundException extends RuntimeException{

    UserNotFoundException(String message){
        super(message);
    }
}
