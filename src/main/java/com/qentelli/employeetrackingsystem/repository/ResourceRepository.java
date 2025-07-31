package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.ResourceType;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

	// 📦 Fetch resources by type with pagination
	Page<Resource> findByResourceType(ResourceType resourceType, Pageable pageable);

	// 🔍 Search resources by ResourceType and name with pagination
	@Query("""
			    SELECT r FROM Resource r
			    WHERE r.resourceType = :type AND (
			          (:type = com.qentelli.employeetrackingsystem.entity.ResourceType.TECH_STACK AND LOWER(r.techStack) LIKE LOWER(CONCAT('%', :name, '%')))
			       OR (:type = com.qentelli.employeetrackingsystem.entity.ResourceType.PROJECT AND LOWER(r.project.projectName) LIKE LOWER(CONCAT('%', :name, '%')))
			    )
			""")
	Page<Resource> searchByResourceTypeAndName(@Param("type") ResourceType type, @Param("name") String name,
			Pageable pageable);
}
