package com.qentelli.employeetrackingsystem.models.client.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeekRangeResponse {

    private int weekId;
    private LocalDate weekFromDate;
    private LocalDate weekToDate;

    private Status statusFlag;
    private EnableStatus enableStatus;
    
    public WeekRangeResponse(int weekId, LocalDate weekFromDate, LocalDate weekToDate) {
        this.weekId = weekId;
        this.weekFromDate = weekFromDate;
        this.weekToDate = weekToDate;
    }
}