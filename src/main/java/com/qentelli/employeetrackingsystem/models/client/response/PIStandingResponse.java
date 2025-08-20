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
public class PIStandingResponse {
    private Long id;
    private int piNumber;
    private Integer projectId;
    private String projectName;
    private String feature;
    private boolean sprint0;
    private boolean sprint1;
    private boolean sprint2;
    private boolean sprint3;
    private boolean sprint4;
    private double completionPercentage;
    private String statusReport;
    private List<String> selectedSprints;
}