package com.qentelli.employeetrackingsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Roles;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

	public boolean existsByEmail(String email);
	public boolean existsByEmployeeCode(String employeeCode);
	public List<Person> findByRole(Roles role);	
	public Page<Person> findByRoleAndPersonStatusTrue(Roles role, Pageable pageable);	
	public List<Person> findByProjectsContaining(Project project);	
	public Optional<Person> findByPersonId(Integer personId);		 
	public Page<Person> findByProjects_ProjectId(Integer projectId, Pageable pageable);		 
	public Page<Person> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
			    String firstName, String lastName, Pageable pageable);	
	public Optional<Person> findByEmail(String email);
}