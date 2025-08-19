package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // ✅ Core fetch methods
    List<Resource> findBySprint_SprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag);
    List<Resource> findByStatusFlag(StatusFlag statusFlag);

    // ✅ Pagination support
    Page<Resource> findBySprint_SprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag, Pageable pageable);
    Page<Resource> findBySprint_SprintId(Long sprintId, Pageable pageable);

    // ✅ Filtering by type
    Page<Resource> findByResourceTypeAndStatusFlag(ResourceType resourceType, StatusFlag statusFlag, Pageable pageable);
    Page<Resource> findBySprint_SprintIdAndResourceTypeAndStatusFlag(Long sprintId, ResourceType resourceType, StatusFlag statusFlag, Pageable pageable);

    // ✅ Filtering by tech stack
    Page<Resource> findByStatusFlagAndResourceTypeAndTechStack(StatusFlag statusFlag, ResourceType resourceType, TechStack techStack, Pageable pageable);
    Page<Resource> findBySprint_SprintIdAndResourceTypeAndTechStackAndStatusFlag(Long sprintId, ResourceType resourceType, TechStack techStack, StatusFlag statusFlag, Pageable pageable);

    // ✅ Filtering by project name
    Page<Resource> findByStatusFlagAndResourceTypeAndProject_ProjectNameContainingIgnoreCase(StatusFlag statusFlag, ResourceType resourceType, String projectName, Pageable pageable);
    Page<Resource> findBySprint_SprintIdAndResourceTypeAndProject_ProjectNameContainingIgnoreCaseAndStatusFlag(Long sprintId, ResourceType resourceType, String projectName, StatusFlag statusFlag, Pageable pageable);
}