package com.qentelli.employeetrackingsystem.models.client.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class WeeklySummaryResponse {
	private int weekId;
	private LocalDate weekStartDate;
	private LocalDate weekEndDate;
	private List<String> upcomingTasks;
	private boolean status;
	private List<String> projectNames;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
}
