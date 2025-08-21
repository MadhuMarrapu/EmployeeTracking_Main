package com.qentelli.employeetrackingsystem.exception;
public class DuplicatePersonException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicatePersonException(String message) {
        super(message);
    }
}