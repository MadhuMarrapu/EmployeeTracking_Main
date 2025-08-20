package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;

import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

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
    private Status statusFlag ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnableStatus enableStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
}