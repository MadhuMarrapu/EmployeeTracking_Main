package com.qentelli.employeetrackingsystem.models.client.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String projectName;    // Optional: If you want to show more project info
    private Long sprintId;         // ✅ Newly added
    private String sprintName;

    private Status statusFlag; // ✅ Lifecycle state
}