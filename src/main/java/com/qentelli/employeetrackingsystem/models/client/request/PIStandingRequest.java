package com.qentelli.employeetrackingsystem.models.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PIStandingRequest {
	private Long id; // null on create
	private int piNumber; // 1‑4
	private Integer projectId;

	private String feature;

	/** Sprint marker (e.g., SPRINT_0 to SPRINT_4) */
	private String selectedSprint; // e.g., "Sprint-1"

	private double completionPercentage;
	private String statusReport;

}
