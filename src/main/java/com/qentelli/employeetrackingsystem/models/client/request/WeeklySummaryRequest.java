package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class WeeklySummaryRequest {
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private List<String> upcomingTasks;
    private boolean status;
    private List<Integer> projectIds;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
