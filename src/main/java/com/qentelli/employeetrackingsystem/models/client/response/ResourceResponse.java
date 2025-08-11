package com.qentelli.employeetrackingsystem.models.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceResponse {

	private Long resourceId;
	private ResourceType resourceType;
	private TechStack techStack;

	private Integer projectId;
	private String projectName;

	private Long sprintId; // 🆕 Added for sprint reference
	private String sprintName; // 🆕 Optional, for display

	private int onsite;
	private int offsite;
	private int total;
	private int totalOnsiteCount;
	private int totalOffsiteCount;
	private String totalRatio;
	private String ratio;
}