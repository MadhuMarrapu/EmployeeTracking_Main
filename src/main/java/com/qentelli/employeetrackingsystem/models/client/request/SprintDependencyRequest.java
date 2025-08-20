package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SprintDependencyRequest {
    private Long sprintId;
    private String type;           
    private String description;
    private String owner;
    private LocalDate date;
    private String statusIn;       
    private String impact;
    private String actionTaken;
    private Integer projectId;     

}