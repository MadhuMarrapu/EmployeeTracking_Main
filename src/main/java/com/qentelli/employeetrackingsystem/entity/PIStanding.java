package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.qentelli.employeetrackingsystem.entity.enums.SprintOrdinal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class PIStanding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Which Program‑Increment / Quarter (1 – 4) */
    @Column(nullable = false)
    private int piNumber;

    /** The project that owns this feature (was “Team” column) */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "projectId")
    private Project project;

    private String feature; // “Rebate Tool” …

    /** Active sprint marker (replaces sprint0–sprint4 booleans) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SprintOrdinal currentSprint;

    private double completionPercentage; // 99

    private String statusReport; // Waiting for Release

    @CreatedDate
    private LocalDateTime createdAt;

    private boolean softDelete = false;
}