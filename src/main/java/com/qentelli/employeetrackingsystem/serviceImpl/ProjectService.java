package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.exception.AccountNotFoundException;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.repository.AccountRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Project createProject(ProjectDetailsDto dto) {
        Project project = modelMapper.map(dto, Project.class);

        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getAccountId()));
            project.setAccount(account);
        }

        return projectRepository.save(project);
    }

    public List<ProjectDetailsDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> {
                    ProjectDetailsDto dto = modelMapper.map(project, ProjectDetailsDto.class);
                    if (project.getAccount() != null) {
                        dto.setAccountId(project.getAccount().getAccountId());
                        dto.setAccountName(project.getAccount().getAccountName());
                    }
                    return dto;
                })
                .toList();
    }

    public ProjectDetailsDto getProjectById(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        ProjectDetailsDto dto = modelMapper.map(project, ProjectDetailsDto.class);

        if (project.getAccount() != null) {
            dto.setAccountId(project.getAccount().getAccountId());
            dto.setAccountName(project.getAccount().getAccountName());
        }

        return dto;
    }

    public Project updateProject(int id, ProjectDetailsDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));

        modelMapper.map(dto, existing);

        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getAccountId()));
            existing.setAccount(account);
        }

        return projectRepository.save(existing);
    }

    @Transactional
    public Project partialUpdateProject(int id, ProjectDetailsDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));

        if (dto.getProjectName() != null) project.setProjectName(dto.getProjectName());
        if (dto.getCreatedBy() != null) project.setCreatedBy(dto.getCreatedBy());
        if (dto.getUpdatedBy() != null) project.setUpdatedBy(dto.getUpdatedBy());
        if (dto.getCreatedAt() != null) project.setCreatedAt(dto.getCreatedAt());
        if (dto.getUpdatedAt() != null) project.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getAccountId()));
            project.setAccount(account);
        }

        return projectRepository.save(project);
    }

    public Project softDeleteProject(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        project.setSoftDelete(true);
        return projectRepository.save(project);
    }

    public Project deleteProject(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
        return project;
    }
}