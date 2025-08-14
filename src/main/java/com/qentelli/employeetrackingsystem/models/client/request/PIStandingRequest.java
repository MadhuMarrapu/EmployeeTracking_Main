package com.qentelli.employeetrackingsystem.models.client.request;

import java.util.List;

import com.qentelli.employeetrackingsystem.entity.enums.SprintOrdinal;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PIStandingRequest {
    private Long id;
    private int piNumber;
    private Integer projectId;
    private String feature;
    private List<SprintOrdinal> selectedSprints;
    private double completionPercentage;
    private String statusReport;
}