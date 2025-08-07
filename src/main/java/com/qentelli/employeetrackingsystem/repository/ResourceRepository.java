package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.TechStack;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    // 📦 Fetch active resources by type with pagination
    Page<Resource> findByResourceTypeAndResourceStatus(ResourceType resourceType, Boolean resourceStatus, Pageable pageable);

    // 🔍 For TECH_STACK search (enum match)
    Page<Resource> findByResourceStatusTrueAndResourceTypeAndTechStack(ResourceType resourceType, TechStack techStack, Pageable pageable);

    // 🔍 For PROJECT search (partial name match, case-insensitive)
    Page<Resource> findByResourceStatusTrueAndResourceTypeAndProject_ProjectNameContainingIgnoreCase(ResourceType resourceType, String projectName, Pageable pageable);

    // 🧾 Fetch all active resources
    List<Resource> findByResourceStatus(Boolean resourceStatus);

    // 🆕 Sprint-aware queries

    // 🔹 All resources by sprint
    Page<Resource> findBySprint_SprintId(Long sprintId, Pageable pageable);

    // 🔹 Active resources by type and sprint
    Page<Resource> findBySprint_SprintIdAndResourceTypeAndResourceStatusTrue(Long sprintId, ResourceType resourceType, Pageable pageable);

    // 🔹 Active TECH_STACK by sprint
    Page<Resource> findBySprint_SprintIdAndResourceTypeAndTechStackAndResourceStatusTrue(Long sprintId, ResourceType resourceType, TechStack techStack, Pageable pageable);

    // 🔹 Active PROJECTs by sprint and name
    Page<Resource> findBySprint_SprintIdAndResourceTypeAndProject_ProjectNameContainingIgnoreCaseAndResourceStatusTrue(
        Long sprintId, ResourceType resourceType, String projectName, Pageable pageable);
}