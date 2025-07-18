package com.qentelli.employeetrackingsystem.models.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySprintUpdateDto {
    private Integer weekSprintId;

    // Using IDs to keep DTO lightweight and avoid deep entity references
    private int weeekRangeId; // ✅ clear and camelCase; // Week Range ID;
    private int projectId;

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
    private int difficultCount1;
    private int difficultCount2;

    private boolean weeklySprintUpdateStatus=true; // true means active, false means inactive;
}