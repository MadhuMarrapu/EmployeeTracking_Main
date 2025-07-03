package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.util.List;

import com.qentelli.employeetrackingsystem.entity.TaskStatus;

import lombok.Data;

@Data
public class ViewReportRequest {
	private int viewReportId;
	private int weekId;
	private int projectId;
	private String employeeId;
	private String taskName;
	private TaskStatus taskStatus;
	private List<String> summary;
	private List<String> keyAccomplishment;
	private List<String> comments;
	private LocalDate taskStartDate;
	private LocalDate taskEndDate;

}
