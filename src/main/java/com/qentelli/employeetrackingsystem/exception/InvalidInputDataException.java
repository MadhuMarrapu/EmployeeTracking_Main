package com.qentelli.employeetrackingsystem.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputDataException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	

	private HttpStatus status;

	public InvalidInputDataException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
