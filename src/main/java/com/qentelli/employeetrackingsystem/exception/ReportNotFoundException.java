package com.qentelli.employeetrackingsystem.exception;
public class ReportNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReportNotFoundException(String message) {
        super(message);
    }
}