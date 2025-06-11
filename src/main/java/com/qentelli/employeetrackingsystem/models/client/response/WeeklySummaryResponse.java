package com.qentelli.employeetrackingsystem.models.client.response;

import lombok.Data;

import java.util.List;

@Data
public class WeeklySummaryResponse {
    private Long id;
    private String weekStartDate;
    private String weekEndDate;
    private List<String> projectNames;
    private List<String> employeeNames;
}
