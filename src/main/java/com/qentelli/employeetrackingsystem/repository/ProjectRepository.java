package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    boolean existsByProjectName(String projectName);
    Page<Project> findByProjectNameContainingIgnoreCase(String projectName, Pageable pageable);
    List<Project> findByStatusFlag(Status statusFlag);
    Page<Project> findByStatusFlag(Status statusFlag, Pageable pageable);
    Page<Project> findByProjectNameContainingIgnoreCaseAndStatusFlag(String name, Status statusFlag, Pageable pageable);
}