package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.RagStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProgressReportDTO {
    private Long reportId;
    private Integer projectId;
    private String projectName; 
    private String teamLead;
    private Integer assignedSP;
    private Integer completedSP;
    private RagStatus rag;
    private Double completionPercentage;
    private LocalDateTime snapshotDate;
}