package org.example.jwtauth.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.jwtauth.common.CommonSubError;
import jakarta.validation.ConstraintViolationException;
import org.example.jwtauth.globalErrorHandler.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.*;


@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleFieldValidation(ValidationException ex, WebRequest request) {
        log.warn("{}: {}", request.getContextPath(), ex.getFieldErrors());

        return new ResponseEntity<>(createResponse(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UserAlreadyExistError.class)
    public ResponseEntity<ErrorResponse> handleRegisterUserError(UserAlreadyExistError ex, WebRequest request) {
        log.warn("{}: {}", request.getContextPath(), ex.getMessage());
        return new ResponseEntity<>(createResponse(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonSubError> handleConstraintValidationException(final ConstraintViolationException ex, WebRequest request) {
        log.error("{}:{}", request.getContextPath(), ex.getMessage());

        return new ResponseEntity<>(createResponse(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex, WebRequest request) {
        log.warn("{}: {}", request.getContextPath(), ex.getMessage());

        return new ResponseEntity<>(createResponse(ex), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> methodValidationErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((errors) -> {
            String fieldName = ((FieldError) errors).getField();
            String errorMessage = ex.getMessage();

            methodValidationErrors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(methodValidationErrors, HttpStatus.BAD_REQUEST);
    }


    private ErrorResponse createResponse(final Throwable ex) {
        return new ErrorResponse(ex.getMessage());

    }

    private ErrorResponse createResponse(final ValidationException ex) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        for (FieldError error : ex.getFieldErrors()) {
            message.add(error.getField() + " " + error.getDefaultMessage());
        }

        return new ErrorResponse(message);
    }

    private CommonSubError createResponse(final ConstraintViolationException ex) {
        List<CommonSubError> errors = new ArrayList<>();

        ex.getConstraintViolations()
                .forEach(constraintViolation -> errors.add(
                        CommonSubError.builder()
                                .message(constraintViolation.getMessage())
                                .field(StringUtils.substringAfterLast(constraintViolation.getPropertyPath().toString(), "."))
                                .value(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null)
                                .type(constraintViolation.getInvalidValue().getClass().getSimpleName())
                                .build()
                ));

        return (CommonSubError) Collections.singleton(errors);
    }

}
