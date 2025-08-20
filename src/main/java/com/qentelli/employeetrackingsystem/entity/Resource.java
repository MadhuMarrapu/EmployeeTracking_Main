package com.qentelli.employeetrackingsystem.entity;

import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

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
    private ResourceType resourceType; 
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
    private String ratio;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status statusFlag; 
    @Transient
    public int getTotal() {
        return onsite + offsite;
    }
    public void enforceTypeConstraints() {
        if (this.resourceType == ResourceType.TECHSTACK) {
            this.project = null;
        } else if (this.resourceType == ResourceType.PROJECT) {
            this.techStack = null;
        }
    }
}