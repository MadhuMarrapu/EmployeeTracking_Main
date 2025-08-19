package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.enums.Roles;
import com.qentelli.employeetrackingsystem.models.client.request.PersonDTO;

public interface PersonService {

    PersonDTO create(PersonDTO dto);
    PersonDTO update(Integer id, PersonDTO dto);
    void softDeletePersonById(Integer personId);
    void tagProjectsToEmployee(Integer personId, List<Integer> projectIds);

    Page<PersonDTO> searchPersonsByName(String name, Pageable pageable);
    List<PersonDTO> getAllResponses();
    PersonDTO getByIdResponse(Integer id);
    List<PersonDTO> getByRoleResponse(Roles role);
    Page<PersonDTO> getByRoleResponse(Roles role, Pageable pageable);
    Page<PersonDTO> getPersonsByProjectId(Integer projectId, Pageable pageable);

    boolean isProjectExists(Integer projectId);
    Person getPersonEntity(String email);
}