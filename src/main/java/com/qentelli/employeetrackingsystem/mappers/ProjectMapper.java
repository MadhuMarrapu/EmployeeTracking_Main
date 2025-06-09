package com.qentelli.employeetrackingsystem.mappers;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDto;

public class ProjectMapper {
	
	// Prevent instantiation
    private ProjectMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // DTO → Entity
    public static Project toEntity(ProjectDto dto) {
        Project project = new Project();
        project.setProjectName(dto.getProjectName());
        project.setProjectLocation(dto.getProjectLocation());
        project.setProjectEndDate(dto.getProjectEndDate());
        project.setAction(dto.getAction());
        // projectStartDate is automatically set by @CreationTimestamp
        return project;
    }

    // Entity → DTO
    public static ProjectDto toDto(Project entity) {
        ProjectDto dto = new ProjectDto();
        dto.setProjectName(entity.getProjectName());
        dto.setProjectLocation(entity.getProjectLocation());
        dto.setProjectEndDate(entity.getProjectEndDate());
        dto.setAction(entity.getAction());
        return dto;
    }
}
