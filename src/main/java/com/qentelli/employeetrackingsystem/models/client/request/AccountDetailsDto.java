package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
//import com.qentelli.employeetrackingsystem.entity.Project;

import com.qentelli.employeetrackingsystem.entity.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsDto {

	private String accountName;
	private LocalDate accountStartDate;
	private LocalDate accountEndDate;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
	private List<Project> projects;
	

 }
