package com.qentelli.employeetrackingsystem.models.client.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmployeeDTO {
    private String employeeId;
    private String employeeName;
    private String summary;
    private String projectName;
    private String techstack;
    private String projectId;
    private List<DailyUpdateDTO> dailyUpdates;
}
