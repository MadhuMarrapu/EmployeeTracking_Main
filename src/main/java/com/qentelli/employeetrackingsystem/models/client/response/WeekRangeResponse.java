package com.qentelli.employeetrackingsystem.models.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekRangeResponse {
    private int id;
    private LocalDate weekFromDate;
    private LocalDate weekToDate;
}

