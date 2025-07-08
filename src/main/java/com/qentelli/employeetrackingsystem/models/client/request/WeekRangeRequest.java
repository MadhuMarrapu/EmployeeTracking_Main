package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WeekRangeRequest {
	
	@NotNull(message = "WeekFromDate must not be null")
	private LocalDate weekFromDate;
	
	@NotNull(message = "WeekToDate must not be null")
	private LocalDate weekToDate;
}