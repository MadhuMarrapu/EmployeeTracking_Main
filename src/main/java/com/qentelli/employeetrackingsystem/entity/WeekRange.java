package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;

import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weekId;

    private LocalDate weekFromDate;
    private LocalDate weekToDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFlag statusFlag = StatusFlag.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnableStatus enableStatus = EnableStatus.DISABLED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
}