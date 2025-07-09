package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeekRangeRequest {
	
	@NotNull(message = "WeekFromDate must not be null")
	private LocalDate weekFromDate;
	
	@NotNull(message = "WeekToDate must not be null")
	private LocalDate weekToDate;
}