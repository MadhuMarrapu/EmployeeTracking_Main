package com.qentelli.employeetrackingsystem.exception;

public class SprintNotFoundException extends RuntimeException {

    public SprintNotFoundException(Long sprintId) {
        super("Sprint not found with ID: " + sprintId);
    }

    public SprintNotFoundException(String message) {
        super(message);
    }
}