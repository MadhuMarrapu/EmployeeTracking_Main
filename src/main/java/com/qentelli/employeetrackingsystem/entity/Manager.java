package com.qentelli.employeetrackingsystem.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer managerId;
	private String firstName;
	private String lastName;
	private String email;

	private String employeeId;
	private String password;
	private String confirmPassword;

	@Enumerated(EnumType.STRING)
	private Roles role;

	// one manage can have multiple projects
<<<<<<< HEAD
	@OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Project> projects = new ArrayList<>();
=======
//	@OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
//	private List<Project> projects;
>>>>>>> 7c12d92743737efb9d27729fe47a2cd9ef1f8869

	@Enumerated(EnumType.STRING)
	private TechStack techStack;

}