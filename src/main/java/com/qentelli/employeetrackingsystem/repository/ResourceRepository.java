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

	public List<Resource> findBySprint_SprintIdAndResourceStatusTrue(Long sprintId);
	public Page<Resource> findBySprint_SprintIdAndResourceStatusTrue(Long sprintId, Pageable pageable);
	public Page<Resource> findByResourceTypeAndResourceStatus(ResourceType resourceType, Boolean resourceStatus,
			Pageable pageable);
	public Page<Resource> findByResourceStatusTrueAndResourceTypeAndTechStack(ResourceType resourceType, TechStack techStack,
			Pageable pageable);
	public Page<Resource> findByResourceStatusTrueAndResourceTypeAndProject_ProjectNameContainingIgnoreCase(
			ResourceType resourceType, String projectName, Pageable pageable);
	public List<Resource> findByResourceStatus(Boolean resourceStatus);
	public Page<Resource> findBySprint_SprintId(Long sprintId, Pageable pageable);
	public Page<Resource> findBySprint_SprintIdAndResourceTypeAndResourceStatusTrue(Long sprintId, ResourceType resourceType,
			Pageable pageable);
	public Page<Resource> findBySprint_SprintIdAndResourceTypeAndTechStackAndResourceStatusTrue(Long sprintId,
			ResourceType resourceType, TechStack techStack, Pageable pageable);
	public Page<Resource> findBySprint_SprintIdAndResourceTypeAndProject_ProjectNameContainingIgnoreCaseAndResourceStatusTrue(
			Long sprintId, ResourceType resourceType, String projectName, Pageable pageable);
}