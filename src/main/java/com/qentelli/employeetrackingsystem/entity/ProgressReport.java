package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "progress_report_id") // foreign key in Project table
	private List<Project> projects;
	private String teamLead;

	private Integer assignedSP;
	private Integer completedSP;

	@Enumerated(EnumType.STRING)
	private RagStatus rag;

	private Double completionPercentage;

	private Boolean progressReportStatus = true; // true for active, false for inactive

	private LocalDateTime snapshotDate; // Optional tracking date
}