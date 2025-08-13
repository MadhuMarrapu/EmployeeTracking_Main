package com.qentelli.employeetrackingsystem.models.client.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GroupedResourceResponse {
	private List<ResourceResponse> techStackResources;
	private int techStackOnsiteTotal;
    private int techStackOffsiteTotal;
    private String techStackRatio;

	private List<ResourceResponse> projectResources;
	private int projectOnsiteTotal;
    private int projectOffsiteTotal;
    private String projectRatio;

}