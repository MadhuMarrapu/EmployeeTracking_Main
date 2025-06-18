package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewReports {
	
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private int viewReportId;
       
	   private List<String> summary;
	   private List<String> keyAccomplishments; 
	   
	   @Enumerated(EnumType.STRING)
	   private TaskStatus taskStatus;  
	   private List<String> comments;
	   private int projectId;
	   private String taskName;
	   private LocalDate taskStartDate;
	   private LocalDate taskEndDate;
	   private LocalDateTime createdAt;
	   private String createdBy;
	   private LocalDateTime updatedAt;
	   private String updatedBy;
	}

