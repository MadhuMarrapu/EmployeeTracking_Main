package com.qentelli.employeetrackingsystem.models.client.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateWeeklySummaryRequest {
    private String weekStartDate;
    private String weekEndDate;
    private List<Integer> projectIds;
    private List<Integer> employeeIds;
}
