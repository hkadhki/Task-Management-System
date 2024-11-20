package com.example.taskmanagersystem.controller;


import com.example.taskmanagersystem.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Locale;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * Handles custom input data errors.
     */
    @ExceptionHandler(ErrorInputDataException.class)
    public ResponseStatusException errorInputDataException(ErrorInputDataException e) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handles unauthorized access errors.
     */
    @ExceptionHandler(UnauthorizedErrorException.class)
    public ResponseStatusException unauthorizedErrorException(UnauthorizedErrorException e) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    /**
     * Handles errors when the HTTP request is unreadable.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseStatusException httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handles permission errors.
     */
    @ExceptionHandler(ErrorPermissionException.class)
    public ResponseStatusException errorPermissionException(ErrorPermissionException e) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }

    /**
     * Handles authentication credential errors.
     */
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseStatusException authenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException e) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handles SQL-related exceptions.
     */
    @ExceptionHandler(SQLException.class)
    public ResponseStatusException sqlException(SQLException e) {
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * Handles validation errors in request arguments.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseStatusException methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder builder = new StringBuilder(e.getMessage());
        //TODO
        log.error(e.getMessage());
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, builder.substring(builder.lastIndexOf("[") + 1, builder.length() - 3));
    }

    /**
     * Handles constraint violations.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseStatusException constraintViolationException(ConstraintViolationException e) {
        StringBuilder builder = new StringBuilder(e.getMessage());

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, builder.substring(builder.indexOf(".") + 1, builder.length() - 1));
    }


}
