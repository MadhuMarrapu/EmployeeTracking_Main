package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WeeklySummary")
public class WeeklySummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int weekId;
	
	@ManyToMany
	private List<Project> listProject;
	private LocalDate weekStartDate;
	private LocalDate weekEndDate;
	private List<String> upcomingTasks;
	private boolean status;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;


}
