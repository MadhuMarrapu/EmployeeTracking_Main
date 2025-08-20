package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.exception.AccountNotFoundException;
import com.qentelli.employeetrackingsystem.exception.DuplicateProjectException;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDTO;
import com.qentelli.employeetrackingsystem.repository.AccountRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.service.PersonService;
import com.qentelli.employeetrackingsystem.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private static final String PROJECT_NOT_FOUND = "Project not found with id : ";
    private static final String ACCOUNT_NOT_FOUND = "Account not found with id : ";

    private final ProjectRepository projectRepo;
    private final AccountRepository accountRepo;
    private final PersonService personService;
    private final Map<String, Map<String, String>> adminMetadata;
    private final ModelMapper modelMapper;

    @Override
    public ProjectDTO create(ProjectDTO dto) throws DuplicateProjectException {
        if (projectRepo.existsByProjectName(dto.getProjectName())) {
            throw new DuplicateProjectException("A project with this name already exists.");
        }
        Account account = accountRepo.findById(dto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND + dto.getAccountId()));
        Project project = modelMapper.map(dto, Project.class);
        project.setStatusFlag(Status.ACTIVE);
        project.setAccount(account);
        Project saved = projectRepo.save(project);
        ProjectDTO responseDto = modelMapper.map(saved, ProjectDTO.class);
        responseDto.setAccountName(account.getAccountName());
        return responseDto;
    }

    @Override
    public List<ProjectDTO> getAll() {
        return projectRepo.findByStatusFlag(Status.ACTIVE).stream()
                .map(project -> {
                    ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
                    dto.setAccountName(project.getAccount().getAccountName());
                    return dto;
                }).toList();
    }

    @Override
    @Transactional
    public ProjectDTO update(Integer id, ProjectDTO dto) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND + id));
        project.setProjectName(dto.getProjectName());
        project.setUpdatedAt(LocalDateTime.now());
        project.setUpdatedBy(getAuthenticatedUserFullName());
        Project updated = projectRepo.save(project);
        ProjectDTO responseDto = modelMapper.map(updated, ProjectDTO.class);
        responseDto.setAccountName(updated.getAccount().getAccountName());
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteProject(Integer projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND + projectId));
        project.setStatusFlag(Status.INACTIVE);
        project.setUpdatedAt(LocalDateTime.now());
        project.setUpdatedBy(getAuthenticatedUserFullName());
        projectRepo.save(project);
    }

    @Override
    public Page<ProjectDTO> searchProjectsByExactName(String name, Pageable pageable) {
        return projectRepo.findByProjectNameContainingIgnoreCaseAndStatusFlag(name, Status.ACTIVE, pageable)
                .map(project -> {
                    ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
                    dto.setAccountName(project.getAccount().getAccountName());
                    return dto;
                });
    }

    @Override
    public Page<ProjectDTO> getActiveProjects(Pageable pageable) {
        return projectRepo.findByStatusFlag(Status.ACTIVE, pageable)
                .map(project -> {
                    ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
                    dto.setAccountName(project.getAccount().getAccountName());
                    return dto;
                });
    }

    private String getAuthenticatedUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            if (adminMetadata.containsKey(username)) {
                Map<String, String> meta = adminMetadata.get(username);
                return meta.getOrDefault("firstName", "Unknown") + " " + meta.getOrDefault("lastName", "User");
            }
            Person person = personService.getPersonEntity(username);
            if (person != null) {
                return person.getFirstName() + " " + person.getLastName();
            }
        }
        return "System";
    }
}