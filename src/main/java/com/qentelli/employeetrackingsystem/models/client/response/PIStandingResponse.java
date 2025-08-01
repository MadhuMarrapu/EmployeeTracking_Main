package com.qentelli.employeetrackingsystem.models.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PIStandingResponse {
   
	private Long id;
    private int  piNumber;
    private Integer projectId;
    private String projectName;
 
    private String feature;
    private boolean sprint0, sprint1, sprint2, sprint3, sprint4;
    private double completionPercentage;
    private String statusReport;
}
