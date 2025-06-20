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
		project.setCreatedAt(dto.getCreatedAt());
		project.setCreatedBy(dto.getCreatedBy());
		project.setUpdatedAt(dto.getUpdatedAt());
		project.setUpdatedBy(dto.getUpdatedBy());
		return project;
	}

	// Entity → DTO
	public static ProjectDetailsDto toDto(Project entity) {
		ProjectDetailsDto dto = new ProjectDetailsDto();
		dto.setProjectName(entity.getProjectName());
		dto.setLocation(entity.getLocation());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setUpdatedBy(entity.getUpdatedBy());
		return dto;
	}
}
