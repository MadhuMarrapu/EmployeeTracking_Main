package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Manager;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.models.client.request.ManagerDTO;
import com.qentelli.employeetrackingsystem.repository.ManagerRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll().stream().map(manager -> {
            ManagerDTO dto = modelMapper.map(manager, ManagerDTO.class);
            dto.setProjectIds(manager.getProjects()
                .stream()
                .map(Project::getProjectId)
                .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public ManagerDTO getManagerById(Integer managerId) {
        return managerRepository.findById(managerId).map(manager -> {
            ManagerDTO dto = modelMapper.map(manager, ManagerDTO.class);
            dto.setProjectIds(manager.getProjects()
                .stream()
                .map(Project::getProjectId)
                .collect(Collectors.toList()));
            return dto;
        }).orElse(null);
    }

    public ManagerDTO createManager(ManagerDTO dto) {
        Manager manager = modelMapper.map(dto, Manager.class);

        if (dto.getProjectIds() != null) {
            List<Project> projects = projectRepository.findAllById(dto.getProjectIds());
            manager.setProjects(projects);
        }

        Manager savedManager = managerRepository.save(manager);
        ManagerDTO savedDto = modelMapper.map(savedManager, ManagerDTO.class);
        savedDto.setProjectIds(savedManager.getProjects()
            .stream()
            .map(Project::getProjectId)
            .collect(Collectors.toList()));
        return savedDto;
    }

    public void deleteManager(Integer managerId) {
        managerRepository.deleteById(managerId);
    }

    public ManagerDTO updateManager(Integer managerId, ManagerDTO dto) {
        Manager existingManager = managerRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found"));

        existingManager.setFirstName(dto.getFirstName());
        existingManager.setLastName(dto.getLastName());
        existingManager.setEmail(dto.getEmail());
        existingManager.setEmployeeId(dto.getEmployeeId());
        existingManager.setPassword(dto.getPassword());
        existingManager.setConfirmPassword(dto.getConfirmPassword());
        existingManager.setRole(dto.getRole());
        existingManager.setTechStack(dto.getTechStack());

        if (dto.getProjectIds() != null) {
            List<Project> projects = projectRepository.findAllById(dto.getProjectIds());
            existingManager.setProjects(projects);
        }

        Manager updated = managerRepository.save(existingManager);
        return modelMapper.map(updated, ManagerDTO.class);
    }
}