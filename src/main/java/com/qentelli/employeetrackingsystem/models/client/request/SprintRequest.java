package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SprintRequest {

    @NotBlank(message = "Sprint number is required")
    private String sprintNumber;

    @NotBlank(message = "Sprint name is required")
    private String sprintName;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

   
}