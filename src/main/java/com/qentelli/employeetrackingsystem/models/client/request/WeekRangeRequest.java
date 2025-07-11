package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WeekRangeRequest {
    private LocalDate weekFromDate;
    private LocalDate weekToDate;
}