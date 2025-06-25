package com.qentelli.employeetrackingsystem.serviceImpl;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.exception.AccountNotFoundException;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.repository.AccountRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public Project createProject(ProjectDetailsDto dto) {
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getAccountId()));

        Project project = modelMapper.map(dto, Project.class);
        project.setAccount(account);
        return projectRepository.save(project);
    }

    // GET ALL
    public List<ProjectDetailsDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(project -> {
                    ProjectDetailsDto dto = modelMapper.map(project, ProjectDetailsDto.class);
                    dto.setAccountId(project.getAccount().getAccountId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // GET BY ID
    public ProjectDetailsDto getProjectById(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        ProjectDetailsDto dto = modelMapper.map(project, ProjectDetailsDto.class);
        dto.setAccountId(project.getAccount().getAccountId());
        return dto;
    }

    // FULL UPDATE
    public Project updateProject(int id, ProjectDetailsDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getAccountId()));
        modelMapper.map(dto, project);
        project.setAccount(account);
        return projectRepository.save(project);
    }

    // PARTIAL UPDATE
    @Transactional
    public Project partialUpdateProject(int id, ProjectDetailsDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));

        if (dto.getProjectName() != null) project.setProjectName(dto.getProjectName());
        if (dto.getAccountId() != null) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + dto.getAccountId()));
            project.setAccount(account);
        }
        if (dto.getCreatedAt() != null) project.setCreatedAt(dto.getCreatedAt());
        if (dto.getCreatedBy() != null) project.setCreatedBy(dto.getCreatedBy());
        if (dto.getUpdatedAt() != null) project.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getUpdatedBy() != null) project.setUpdatedBy(dto.getUpdatedBy());

        return projectRepository.save(project);
    }

    // SOFT DELETE
    public Project softDeleteProject(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        project.setSoftDelete(true);
        return projectRepository.save(project);
    }

    // HARD DELETE
    public Project deleteProject(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
        projectRepository.deleteById(id);
        return project;
    }
}