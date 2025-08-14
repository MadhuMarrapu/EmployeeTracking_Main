package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.qentelli.employeetrackingsystem.entity.enums.SprintOrdinal;

import jakarta.persistence.*;

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

    @Column(nullable = false)
    private int piNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "projectId")
    private Project project;

    private String feature;

    @ElementCollection(targetClass = SprintOrdinal.class)
    @CollectionTable(name = "pi_selected_sprints", joinColumns = @JoinColumn(name = "pi_standing_id"))
    @Column(name = "sprint")
    @Enumerated(EnumType.STRING)
    private List<SprintOrdinal> selectedSprints = new ArrayList<>();

    private double completionPercentage;

    private String statusReport;

    @CreatedDate
    private LocalDateTime createdAt;

    private boolean softDelete = false;
}