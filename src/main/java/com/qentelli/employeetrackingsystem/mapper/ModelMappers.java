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
	        project.setCreatedAt(dto.getCreatedAt());
	        project.setCreatedBy(dto.getCreatedBy());
	        project.setUpdatedAt(dto.getUpdatedAt());
	        project.setUpdatedBy(dto.getUpdatedBy());
	        project.setAccount(dto.getAccount());
	        
//	        if (dto.getAccount() != null) {
//				project.setAccount(AccountMapper.toEntity(dto.getAccount())); // ✅ full Account mapping
//			}

	        return project;
	    }
	 
	    // Entity → DTO
	    public static ProjectDetailsDto toDto(Project entity) {
	        ProjectDetailsDto dto = new ProjectDetailsDto();
	        dto.setProjectName(entity.getProjectName());
	        dto.setCreatedAt(entity.getCreatedAt());
	        dto.setCreatedBy(entity.getCreatedBy());
	        dto.setUpdatedAt(entity.getUpdatedAt());
	        dto.setUpdatedBy(entity.getUpdatedBy());
	        dto.setAccount(entity.getAccount());
//	        if (entity.getAccount() != null) {
//				dto.setAccount(AccountMapper.toDto(entity.getAccount())); // ✅ full Account DTO
//			}

	        return dto;
	    }
	}


