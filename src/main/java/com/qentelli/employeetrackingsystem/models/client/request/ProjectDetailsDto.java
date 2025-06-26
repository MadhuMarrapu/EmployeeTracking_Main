package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDateTime;

import com.qentelli.employeetrackingsystem.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailsDto {
	private Integer projectId;
	private String projectName;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy; 
	private Integer accountId;
	private String accountName;
}