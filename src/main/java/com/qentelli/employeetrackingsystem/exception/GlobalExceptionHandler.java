package com.qentelli.employeetrackingsystem.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        AuthResponse<Object> response = new AuthResponse<>(
            HttpStatus.UNAUTHORIZED.value(),
            RequestProcessStatus.FAILURE,
            LocalDateTime.now(),
            "Login failed",
            null
        );
        response.setErrorCode(HttpStatus.UNAUTHORIZED);
        response.setErrorDescription("Invalid username or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AuthResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        AuthResponse<Object> response = new AuthResponse<>(
            HttpStatus.NOT_FOUND.value(),
            RequestProcessStatus.FAILURE,
            LocalDateTime.now(),
            "User lookup failed",
            null
        );
        response.setErrorCode(HttpStatus.NOT_FOUND);
        response.setErrorDescription("User not found with the provided username");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<AuthResponse<Object>> handleManagerNotFoundException(PersonNotFoundException ex, HttpServletRequest request) {
       
        AuthResponse<Object> response = new AuthResponse<>(
            HttpStatus.NOT_FOUND.value(),
            RequestProcessStatus.FAILURE,
            LocalDateTime.now(),
            "person not found for request path: " + request.getRequestURI(),
            null
        );
        response.setErrorCode(HttpStatus.NOT_FOUND);
        response.setErrorDescription(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicatePersonException.class)
    public ResponseEntity<AuthResponse<Object>> handleDuplicatePerson(DuplicatePersonException ex) {
        AuthResponse<Object> response = new AuthResponse<>(
            HttpStatus.CONFLICT.value(),
            RequestProcessStatus.FAILURE,
            LocalDateTime.now(),
            "person creation failed",
            null
        );
        response.setErrorCode(HttpStatus.CONFLICT);
        response.setErrorDescription(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}