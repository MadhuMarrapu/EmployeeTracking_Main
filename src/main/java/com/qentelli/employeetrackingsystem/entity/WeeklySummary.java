package com.qentelli.employeetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weekStartDate;
    private String weekEndDate;

    @ManyToMany
    @JoinTable(
        name = "weekly_summary_projects",
        joinColumns = @JoinColumn(name = "summary_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> listProject;

    @ManyToMany
    @JoinTable(
        name = "weekly_summary_employees",
        joinColumns = @JoinColumn(name = "summary_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employeeList;
}
