package com.qentelli.employeetrackingsystem.models.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DailyUpdateDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String task;
    private String status;
    private String employeeId;
    private List<String> keyAccomplishments;
}
