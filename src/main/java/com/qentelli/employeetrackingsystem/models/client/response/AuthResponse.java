package com.qentelli.employeetrackingsystem.models.client.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthResponse<T> {

    private String code;
    private RequestProcessStatus statusType;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private HttpStatusCode errorCode;
    private String errorDescription;

    // Success with message only
    public AuthResponse(Integer code, RequestProcessStatus statusType, String message) {
        this.code = code.toString();
        this.statusType = statusType;
        this.message = message;
    }

    // Success with message and data
    public AuthResponse(Integer code, RequestProcessStatus statusType, LocalDateTime timestamp, String message, T data) {
        this.code = code.toString();
        this.statusType = statusType;
        this.timestamp = timestamp;
        this.message = message;
        this.data = data;
    }

    // Success with message and timestamp (no data)
    public AuthResponse(Integer code, RequestProcessStatus statusType, LocalDateTime timestamp, String message) {
        this.code = code.toString();
        this.statusType = statusType;
        this.timestamp = timestamp;
        this.message = message;
    }

    // Failure with error details
    public AuthResponse(Integer code, RequestProcessStatus statusType, String message, HttpStatusCode errorCode, String errorDescription) {
        this.code = code.toString();
        this.statusType = statusType;
        this.message = message;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }
}