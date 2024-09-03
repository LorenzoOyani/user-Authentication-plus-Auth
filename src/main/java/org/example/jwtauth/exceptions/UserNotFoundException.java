package org.example.jwtauth.exceptions;

public class UserNotFoundException extends RuntimeException{

    UserNotFoundException(String message){
        super(message);
    }
}
