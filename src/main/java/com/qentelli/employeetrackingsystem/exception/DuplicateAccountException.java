package com.qentelli.employeetrackingsystem.exception;
public class DuplicateAccountException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateAccountException(String message) {
        super(message);
    }
}