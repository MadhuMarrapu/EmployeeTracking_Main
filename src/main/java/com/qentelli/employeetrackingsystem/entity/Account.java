package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int accountId;
	@Column(unique = true)
	private String accountName;
	private LocalDate accountStartDate;
	private LocalDate accountEndDate;
	private Boolean softDelete = false;
	@CreatedDate
	private LocalDateTime createdAt;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private LocalDateTime updatedAt;
	@LastModifiedBy
	private String updatedBy;
	
}
