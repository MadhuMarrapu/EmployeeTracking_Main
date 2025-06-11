package com.qentelli.employeetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Transient
    private String confirmPassword;

    @ManyToMany
    @JoinTable(
        name = "employee_project",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> listProject;

    @ManyToMany
    @JoinTable(
        name = "employee_techstack",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "techstack_id")
    )
    private List<TechStack> techStackList;

    @Enumerated(EnumType.STRING)
    private Roles roles;
}
