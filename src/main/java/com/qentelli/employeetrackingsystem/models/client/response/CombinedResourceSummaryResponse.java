package com.qentelli.employeetrackingsystem.models.client.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CombinedResourceSummaryResponse {
    private List<TechStackSummaryResponse> techStackSummary;
    private List<ProjectSummaryResponse> projectSummary;
}