package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Manager;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.models.client.request.ManagerDTO;
import com.qentelli.employeetrackingsystem.repository.ManagerRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private ModelMapper modelMapper;

    public ManagerDTO create(ManagerDTO dto) {
        Manager manager = modelMapper.map(dto, Manager.class);

        List<Project> projects = new ArrayList<>();
        for (Integer id : dto.getProjectIds()) {
            Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
            project.setManager(manager);
            projects.add(project);
        }

        manager.setProjects(projects);

        Manager saved = managerRepo.save(manager);
        return modelMapper.map(saved, ManagerDTO.class);
    }

    public List<ManagerDTO> list() {
        return managerRepo.findAll().stream()
            .map(manager -> modelMapper.map(manager, ManagerDTO.class))
            .toList();
    }

    public ManagerDTO getById(Integer id) {
        Manager manager = managerRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Manager not found"));
        return modelMapper.map(manager, ManagerDTO.class);
    }

    public ManagerDTO update(Integer id, ManagerDTO dto) {
        Manager existing = managerRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Manager not found"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        existing.setRole(Roles.valueOf(dto.getRole().toUpperCase()));
        existing.setTechStack(dto.getTechStack());

        List<Project> projects = new ArrayList<>();
        for (Integer projectId : dto.getProjectIds()) {
            Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
            project.setManager(existing);
            projects.add(project);
        }

        existing.setProjects(projects);

        Manager updated = managerRepo.save(existing);
        return modelMapper.map(updated, ManagerDTO.class);
    }

    public void delete(Integer id) {
        managerRepo.deleteById(id);
    }
}