package org.example.jwtauth.globalErrorHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.List;

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
