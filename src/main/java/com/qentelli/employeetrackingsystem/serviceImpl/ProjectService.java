package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.mapper.ModelMappers;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public Project createProject(ProjectDetailsDto projectDetailsDto) {
		Project project = ModelMappers.toEntity(projectDetailsDto);
		return projectRepository.save(project);
	}

	public List<ProjectDetailsDto> getAllProjects() {
		List<Project> projects = projectRepository.findAll();

		return projects.stream().map(ModelMappers::toDto).collect(Collectors.toList());
	}

	public Project updateProject(int id, ProjectDetailsDto dto) {
		Optional<Project> optionalProject = projectRepository.findById(id);
		if (!optionalProject.isPresent()) {
			throw new RuntimeException("Project not found with id: " + id);
		}

		Project existingProject = optionalProject.get();

		// Full update (ignore startDate if it's auto-managed)
		existingProject.setProjectName(dto.getProjectName());
		existingProject.setLocation(dto.getLocation());
		existingProject.setCreatedAt(dto.getCreatedAt());
		existingProject.setCreatedBy(dto.getCreatedBy());
		existingProject.setUpdatedAt(dto.getUpdatedAt());
		existingProject.setUpdatedBy(dto.getUpdatedBy());
//		existingProject.setAction(dto.getAction());

		return projectRepository.save(existingProject);
	}

	@Transactional
	public Project partialUpdateProject(int id, ProjectDetailsDto dto) {
		System.out.println(dto);
		Optional<Project> optionalProject = projectRepository.findById(id);

		if (!optionalProject.isPresent()) {
			throw new RuntimeException("Project not found with id: " + id);
		}

		Project project = optionalProject.get();
		System.out.println(project);

		// Partial updates - only set fields if not null

		if (dto.getProjectName() != null) {
			project.setProjectName(dto.getProjectName());
		}

		if (dto.getLocation() != null) {
			project.setLocation(dto.getLocation());
		}

//		if (dto.getEndDate() != null) {
//			project.setEndDate(dto.getEndDate());
//		}
		
		if (dto.getCreatedAt() != null) {
			project.setCreatedAt(dto.getCreatedAt());
		}
		
		if (dto.getCreatedBy() != null) {
			project.setCreatedBy(dto.getCreatedBy());
		}
		
		if (dto.getUpdatedAt() != null) {
			project.setUpdatedAt(dto.getUpdatedAt());
		}
		
		if (dto.getUpdatedBy() != null) {
			project.setUpdatedBy(dto.getUpdatedBy());
		}
		
		
//		if (dto.getAction() != null) {
//			project.setAction(dto.getAction());
//		}

		Project save = projectRepository.save(project);
		System.out.println(save);

		return save;
	}
	
	public Project softDeleteProject(int id) {
		
		Project projectFound = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
		projectFound.setSoftDelete(true);
		return projectRepository.save(projectFound);
		
	}
	
	public Project deleteProject(int id) {
		
	    Optional<Project> optionalProject = projectRepository.findById(id);
		if (!projectRepository.existsById(id)) {
			throw new RuntimeException("Project not found with id: " + id);
		}

	    Project project = optionalProject.get();
	    projectRepository.deleteById(id); // delete happens here

	    return project; 

		
	}

}
