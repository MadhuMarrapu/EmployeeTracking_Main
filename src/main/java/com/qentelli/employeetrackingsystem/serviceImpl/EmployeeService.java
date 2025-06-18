package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.mappers.EmployeeMapper;
import com.qentelli.employeetrackingsystem.models.client.request.CreateEmployeeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.EmployeeResponse;
import com.qentelli.employeetrackingsystem.repository.EmployeeRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.TechStackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TechStackRepository techStackRepository;

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Roles role;
        try {
            role = Roles.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        List<Project> projects = projectRepository.findAllById(request.getProjectIds());
        List<TechStack> techStacks = techStackRepository.findAllById(request.getTechStackIds());

        Employee employee = EmployeeMapper.toEntity(request, projects, techStacks, role);
        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
    }
}
