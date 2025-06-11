package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ProjectDetails")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pId;

	private String projectName;

	private String projectLocation;
	
	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDate projectStartDate;

	private LocalDate projectEndDate;
	
	private Boolean action;

}
