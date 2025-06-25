package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String projectName;
    private String projectLocation;
    private LocalDate projectEndDate;
    private Boolean action;
}
