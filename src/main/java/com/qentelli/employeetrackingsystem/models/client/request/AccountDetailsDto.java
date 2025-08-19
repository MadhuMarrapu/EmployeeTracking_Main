package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountDetailsDto {

    private int accountId;
    @NotBlank(message = "Account name is required")
    @Size(max = 20, message = "Account name must not exceed 20 characters")
    private String accountName;
    private LocalDate accountStartDate;
    private LocalDate accountEndDate;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}