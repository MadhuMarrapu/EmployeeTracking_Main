package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.models.client.request.PersonDTO;

public interface PersonService {

	public  PersonDTO create(PersonDTO dto);
	public PersonDTO update(Integer id, PersonDTO dto);
	public void softDeletePersonById(Integer personId);
	public void tagProjectsToEmployee(Integer personId, List<Integer> projectIds);
	public Page<PersonDTO> searchPersonsByName(String name, Pageable pageable);
	public List<PersonDTO> getAllResponses();
	public PersonDTO getByIdResponse(Integer id);
	public List<PersonDTO> getByRoleResponse(Roles role);
	public Page<PersonDTO> getByRoleResponse(Roles role, Pageable pageable);
	public Page<PersonDTO> getPersonsByProjectId(Integer projectId, Pageable pageable);
	public boolean isProjectExists(Integer projectId);
	public Person getPersonEntity(String email);
}