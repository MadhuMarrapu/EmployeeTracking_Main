package com.qentelli.employeetrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.SprintDependency;


@Repository
public interface SprintDependencyRepository extends JpaRepository<SprintDependency, Long> {
    Page<SprintDependency> findByProject_ProjectId(Integer projectId, Pageable pageable);
}
