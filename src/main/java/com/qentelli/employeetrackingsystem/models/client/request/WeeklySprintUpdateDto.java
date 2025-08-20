package com.qentelli.employeetrackingsystem.models.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.HealthStatus;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySprintUpdateDto {

    private Integer weekSprintId;
    private String sprintNumber;
    private int weeekRangeId;
    private int projectId;
    private String projectName;

    private int assignedPoints;
    private int assignedStoriesCount;
    private int inDevPoints;
    private int inDevStoriesCount;
    private int inQaPoints;
    private int inQaStoriesCount;
    private int completePoints;
    private int completeStoriesCount;
    private int blockedPoints;
    private int blockedStoriesCount;
    private double completePercentage;

    private String estimationHealth;
    private String groomingHealth;

    @JsonProperty("estimationHealthStatus")
    private HealthStatus estimationHealthStatus;

    @JsonProperty("groomingHealthStatus")
    private HealthStatus groomingHealthStatus;

    private int difficultCount1;
    private int difficultCount2;
    private int riskPoints;
    private int riskStoryCounts;
    private String comments;
    private Integer injectionPercentage;

   

   
}