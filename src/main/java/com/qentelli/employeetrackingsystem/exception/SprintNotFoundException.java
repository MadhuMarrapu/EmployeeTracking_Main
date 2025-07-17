package com.qentelli.employeetrackingsystem.exception;

public class SprintNotFoundException extends RuntimeException {

    public SprintNotFoundException(String message) {
        super(message);
    }

    public SprintNotFoundException(Long sprintId) {
        super("Sprint not found with ID: " + sprintId);
    }

    // Optionally: include a default constructor
    public SprintNotFoundException() {
        super("Sprint not found.");
    }
}