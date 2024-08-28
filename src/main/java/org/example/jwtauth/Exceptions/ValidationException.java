package org.example.jwtauth.Exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

import static java.util.Arrays.stream;

@Getter
public class ValidationException extends RuntimeException {

    private List<FieldError> fieldErrors;

    ValidationException(String message) {
        super(message);
    }

    ValidationException(String message, FieldError fieldError) {
        this(message);
        this.fieldErrors.add(fieldError);

    }
}
