package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.exception.DuplicateProjectException;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDTO;
import com.qentelli.employeetrackingsystem.entity.Project;

public interface ProjectService {

	public ProjectDTO create(ProjectDTO dto) throws DuplicateProjectException;
	public List<ProjectDTO> getAll();
	public ProjectDTO update(Integer id, ProjectDTO dto);
	public void deleteProject(Integer projectId);
	public Page<Project> searchProjectsByExactName(String name, Pageable pageable);
	public Page<Project> getactiveProjects(Pageable pageable);
}