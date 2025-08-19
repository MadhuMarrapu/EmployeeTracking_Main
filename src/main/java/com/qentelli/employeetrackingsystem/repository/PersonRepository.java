package com.qentelli.employeetrackingsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.Roles;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    boolean existsByEmail(String email);
    boolean existsByEmployeeCode(String employeeCode);

    List<Person> findByRole(Roles role);
    Page<Person> findByRoleAndStatusFlag(Roles role, StatusFlag statusFlag, Pageable pageable);

    List<Person> findByProjectsContaining(Project project);
    Optional<Person> findByPersonId(Integer personId);

    Page<Person> findByProjects_ProjectIdAndStatusFlag(Integer projectId, StatusFlag statusFlag, Pageable pageable);

    Page<Person> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndStatusFlag(
            String firstName, String lastName, StatusFlag statusFlag, Pageable pageable);

    Optional<Person> findByEmail(String email);

    List<Person> findByStatusFlag(StatusFlag statusFlag);
}