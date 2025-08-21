package com.qentelli.employeetrackingsystem.exception;

public class SprintNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SprintNotFoundException(Long sprintId) {
        super("Sprint not found with ID: " + sprintId);
    }

    public SprintNotFoundException(String message) {
        super(message);
    }
}