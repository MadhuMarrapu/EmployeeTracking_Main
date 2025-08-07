package com.qentelli.employeetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType; // TECH_STACK or PROJECT

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TechStack techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = true)
    private Sprint sprint;

    private int onsite;
    private int offsite;

    private int totalOnsiteCount;
    private int totalOffsiteCount;
    private String totalRatio;
    private String ratio;

    private Boolean resourceStatus = true;

    @Transient
    public int getTotal() {
        return onsite + offsite;
    }
}