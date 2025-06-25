package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AccountDetailsDto {
    private int accountId;
    private String accountName;
    private LocalDate accountStartDate;
    private LocalDate accountEndDate;
    private Boolean softDelete;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private List<ProjectDetailsDto> projects;
}