package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.SprintDependency;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface SprintDependencyRepository extends JpaRepository<SprintDependency, Long> {

    // 🔹 Lifecycle-aware by project
    Page<SprintDependency> findByProject_ProjectIdAndStatusFlag(Integer projectId, StatusFlag statusFlag, Pageable pageable);

    // 🔹 Lifecycle-aware by sprint
    Page<SprintDependency> findBySprint_SprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag, Pageable pageable);

    // 🔹 Non-paginated lifecycle-aware by sprint
    List<SprintDependency> findBySprint_SprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag);

    // 🔹 Legacy methods (optional: mark deprecated if unused)
    Page<SprintDependency> findByProject_ProjectId(Integer projectId, Pageable pageable);
    Page<SprintDependency> findBySprint_SprintId(Long sprintId, Pageable pageable);
    List<SprintDependency> findBySprint_SprintId(Long sprintId);
    Page<SprintDependency> findByStatusFlag(StatusFlag statusFlag, Pageable pageable);
}