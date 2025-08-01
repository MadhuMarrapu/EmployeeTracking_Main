package com.qentelli.employeetrackingsystem.models.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PIStandingRequest {
	private Long id;            // null on create
    private int  piNumber;      // 1‑4
    private Integer projectId;
 
    private String feature;
    private boolean sprint0, sprint1, sprint2, sprint3, sprint4;
    private double completionPercentage;
    private String statusReport;
}
