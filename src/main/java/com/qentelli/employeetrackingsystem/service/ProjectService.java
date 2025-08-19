package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.exception.DuplicateProjectException;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDTO;

public interface ProjectService {
    ProjectDTO create(ProjectDTO dto) throws DuplicateProjectException;
    List<ProjectDTO> getAll();
    ProjectDTO update(Integer id, ProjectDTO dto);
    void deleteProject(Integer projectId);
    Page<ProjectDTO> searchProjectsByExactName(String name, Pageable pageable);
    Page<ProjectDTO> getActiveProjects(Pageable pageable);
}