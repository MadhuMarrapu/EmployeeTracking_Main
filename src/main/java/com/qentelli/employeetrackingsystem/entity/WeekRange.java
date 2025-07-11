package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int weekId;
    @NotNull(message = "Week start date is required")
    private LocalDate weekStartDate;

    @NotNull(message = "Week end date is required")
    private LocalDate weekEndDate;
	private boolean softDelete = false;
}