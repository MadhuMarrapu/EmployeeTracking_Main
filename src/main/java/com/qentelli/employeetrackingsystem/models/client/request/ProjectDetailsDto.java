package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProjectDetailsDto {
    private Integer projectId;
    private String projectName;
    private Boolean softDelete;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer accountId;   // For mapping back to Account
    private String managerId;    // Assuming Manager entity has a String ID
}