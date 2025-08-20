package com.qentelli.employeetrackingsystem.models.client.response;

import java.time.LocalDate;
import java.util.List;

import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.entity.enums.CloneState;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SprintResponse {
    private Long sprintId;
    private String sprintNumber;
    private String sprintName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<WeekRangeResponse> weeks;
    private Status statusFlag;
    private EnableStatus enableStatus;
    private CloneState cloneState;
}