package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.mappers.ProjectMapper;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDto;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

@Service
public class ProjectService {

	private ProjectRepository projectRepository;

	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public ProjectDto createProject(ProjectDto projectDto) {
		Project project = ProjectMapper.toEntity(projectDto);
		Project saved = projectRepository.save(project);
		return ProjectMapper.toDto(saved);
	}

	public List<ProjectDto> getAllProjects() {
		List<Project> projects = projectRepository.findAll();
		return projects.stream().map(ProjectMapper::toDto).toList(); // returns unmodifiable List<ProjectDto>
	}

}
