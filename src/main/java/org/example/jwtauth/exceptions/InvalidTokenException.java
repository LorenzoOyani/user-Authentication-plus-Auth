package org.example.jwtauth.exceptions;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message){
        super(message);

    }
}
