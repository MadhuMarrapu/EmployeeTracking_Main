package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CreateWeeklySummaryRequest {
	private LocalDate weekStartDate;
	private LocalDate weekEndDate;
	private List<String> upcomingTasks;
	private boolean status;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
}
