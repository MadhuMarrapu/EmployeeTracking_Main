package com.qentelli.employeetrackingsystem.mapper;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;

public class ModelMappers {

		// Prevent instantiation
	    private ModelMappers() {
	        throw new UnsupportedOperationException("Utility class");
	    }
	 
	    // DTO → Entity
	    public static Project toEntity(ProjectDetailsDto dto) {
	        Project project = new Project();
	        project.setProjectName(dto.getProjectName());
	        project.setLocation(dto.getLocation());
//	        project.setEndDate(dto.getEndDate());
	        project.setCreatedAt(dto.getCreatedAt());
	        project.setCreatedBy(dto.getCreatedBy());
	        project.setUpdatedAt(dto.getUpdatedAt());
	        project.setUpdatedBy(dto.getUpdatedBy());
//	        project.setAction(dto.getAction());
	        // projectStartDate is automatically set by @CreationTimestamp
	        return project;
	    }
	 
	    // Entity → DTO
	    public static ProjectDetailsDto toDto(Project entity) {
	        ProjectDetailsDto dto = new ProjectDetailsDto();
	        dto.setProjectName(entity.getProjectName());
	        dto.setLocation(entity.getLocation());
//	        dto.setEndDate(entity.getEndDate());
	        dto.setCreatedAt(entity.getCreatedAt());
	        dto.setCreatedBy(entity.getCreatedBy());
	        dto.setUpdatedAt(entity.getUpdatedAt());
	        dto.setUpdatedBy(entity.getUpdatedBy());
//	        dto.setAction(entity.getAction());
	        return dto;
	    }
	}


