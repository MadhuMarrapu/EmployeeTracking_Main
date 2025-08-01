package com.qentelli.employeetrackingsystem.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;

    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String employeeCode;
    private String password;
    private String confirmPassword;
    
    @Enumerated(EnumType.STRING)
    private Roles role;
    private Boolean personStatus=true; // true for active, false for inactive;
    @ManyToMany
    @JoinTable(
        name = "person_project",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TechStack techStack;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id", referencedColumnName = "resourceId")
    private Resource resource;
}