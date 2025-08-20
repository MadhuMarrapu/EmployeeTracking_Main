package com.qentelli.employeetrackingsystem.exception;

public class PIStandingNotFoundException extends RuntimeException {

    public PIStandingNotFoundException(Long id) {
        super("PIStanding with ID " + id + " not found.");
    }

    public PIStandingNotFoundException(String message) {
        super(message);
    }
}