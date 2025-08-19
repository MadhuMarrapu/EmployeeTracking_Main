package com.qentelli.employeetrackingsystem.models.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SprintDependencyRequest {
    private Long sprintId;
    private String type;           // e.g., Dependency or Blocker
    private String description;
    private String owner;
    private LocalDate date;
    private String statusIn;       // e.g., "In Progress"
    private String impact;
    private String actionTaken;
    private Integer projectId;     // Refers to Project entity

}