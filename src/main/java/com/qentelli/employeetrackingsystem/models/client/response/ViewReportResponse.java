package com.qentelli.employeetrackingsystem.models.client.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.qentelli.employeetrackingsystem.entity.TaskStatus;

import lombok.Data;

@Data
public class ViewReportResponse {
	private int viewReportId;
	private String taskName;
	private TaskStatus taskStatus;
	private List<String> summary;
	private List<String> keyAccomplishment;
	private List<String> comments;
	private String projectName;
	private String employeeName;
	private LocalDate taskStartDate;
	private LocalDate taskEndDate;
	private LocalDateTime createdAt;
	private String createdBy;
}
