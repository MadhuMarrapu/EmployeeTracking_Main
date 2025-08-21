package com.qentelli.employeetrackingsystem.exception;

public class WeekRangeNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WeekRangeNotFoundException(Long id) {
        super("WeekRange not found with ID: " + id);
    }

    public WeekRangeNotFoundException(String message) {
        super(message);
    }
}