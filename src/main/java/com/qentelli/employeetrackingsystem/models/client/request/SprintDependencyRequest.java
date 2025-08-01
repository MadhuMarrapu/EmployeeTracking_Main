package com.qentelli.employeetrackingsystem.models.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SprintDependencyRequest {
    private String type;           // e.g., Dependency or Blocker
    private String description;
    private String owner;
    private LocalDate date;
    private String status;         // e.g., "In Progress"
    private String impact;
    private String actionTaken;
    private Integer projectId;        // Refers to Project entity
}

