package com.qentelli.employeetrackingsystem.models.client.response;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SprintDependencyResponse {
    private Long id;
    private String type;
    private String description;
    private String owner;
    private LocalDate date;
    private String statusIn;
    private String impact;
    private String actionTaken;
    private Integer projectId;
    private String projectName;  // Optional: If you want to show more project info
    private Long sprintId;           // ✅ Newly added
    private String sprintName; 
}

