package com.qentelli.employeetrackingsystem.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
public class WeeklySprintUpdate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weekSprintId;
 
    @ManyToOne
    @JoinColumn(name = "weekId")
    private WeekRange week;
 
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
 
    private int assignedPoints;
    private int assignedStoriesCount;
    private int inDevPoints;
    private int inDevStoriesCount;
    private int inQaPoints;
    private int inQaStoriesCount;
    private int completePoints;
    private int completeStoriesCount;
    private int blockedPoints;
    private int blockedStoriesCount;
    private double completePercentage;
    private String estimationHealth;
    private String groomingHealth;//
    //grpoomingHealth status is a string that can be used to store the health of grooming, such as "Healthy", "Needs Attention", etc.
    private int difficultCount1;
    private int difficultCount2;
    //estination status-red ember,green-constants
    //privtae String riskpoints,riskstorycounts
    //notes-string
    
    private boolean weeklySprintUpdateStatus=true; // true means active, false means inactive;
}
 