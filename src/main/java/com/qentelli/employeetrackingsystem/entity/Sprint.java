package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sprint")
public class Sprint {
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprintId;
    private String sprintNumber;
    private String sprintName;
    private LocalDate fromDate;
    private LocalDate toDate; 
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WeekRange> weeks;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFlag statusFlag = StatusFlag.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnableStatus enableStatus = EnableStatus.DISABLED; 
    
}
