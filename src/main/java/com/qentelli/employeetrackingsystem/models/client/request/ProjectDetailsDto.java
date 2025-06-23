package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.Account;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.Project;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectDetailsDto {

	private int projectId;
	private String projectName;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
	private Account account;
}
