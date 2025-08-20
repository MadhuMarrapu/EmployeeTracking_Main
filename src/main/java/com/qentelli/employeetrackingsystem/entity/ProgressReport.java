package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDateTime;

import com.qentelli.employeetrackingsystem.entity.enums.RagStatus;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    private String teamLead;
    private Integer assignedSP;
    private Integer completedSP;
    @Enumerated(EnumType.STRING)
    private RagStatus rag;
    private Double completionPercentage;
    @Enumerated(EnumType.STRING)
    private Status statusFlag;
    private LocalDateTime snapshotDate; 
}