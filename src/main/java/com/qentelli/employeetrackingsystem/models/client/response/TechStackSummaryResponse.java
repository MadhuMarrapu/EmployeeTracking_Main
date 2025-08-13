package com.qentelli.employeetrackingsystem.models.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TechStackSummaryResponse {
    private TechStack techStack;
    private int onsite;
    private int offsite;
    private int total;
}