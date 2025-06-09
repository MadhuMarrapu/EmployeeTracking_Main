package com.qentelli.employeetrackingsystem.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.models.client.request.CreateEmployeeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.EmployeeResponse;

public class EmployeeMapper {
	
	// Prevent instantiation
    private EmployeeMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Employee toEntity(CreateEmployeeRequest dto, List<Project> projects, List<TechStack> techStacks, Roles role) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setConfirmPassword(dto.getConfirmPassword());
        employee.setListProject(projects);
        employee.setTechStackList(techStacks);
        employee.setRoles(role);
        return employee;
    }

    public static EmployeeResponse toDto(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setProjectNames(
                employee.getListProject().stream()
                        .map(Project::getProjectName)
                        .collect(Collectors.toList())
        );
        response.setTechStackNames(
                employee.getTechStackList().stream()
                        .map(TechStack::getName)
                        .collect(Collectors.toList())
        );
        response.setRoleName(employee.getRoles().name());
        return response;
    }
}
