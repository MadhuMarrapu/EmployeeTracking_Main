
package com.qentelli.employeetrackingsystem.mappers;

import com.qentelli.employeetrackingsystem.entity.*;
import com.qentelli.employeetrackingsystem.models.client.request.*;

import java.util.ArrayList;
import java.util.List;

public abstract class EmployeeMapper {
	
	// Prevent instantiation
		private EmployeeMapper() {
			throw new UnsupportedOperationException("Utility class");
		}

    public static EmployeeDTO toDTO(Employee employee) {
        if (employee == null) return null;

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setSummary(employee.getSummary());
        dto.setProjectName(employee.getProjectName());
        dto.setTechstack(employee.getTechstack());
        dto.setProjectId(employee.getProject() != null ? employee.getProject().getId().toString() : null);

        if (employee.getDailyUpdates() != null) {
            List<DailyUpdateDTO> dtoList = new ArrayList<>();
            for (DailyUpdate d : employee.getDailyUpdates()) {
                dtoList.add(DailyUpdateMapper.toDTO(d));
            }
            dto.setDailyUpdates(dtoList);
        }

        return dto;
    }

    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) return null;
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setSummary(dto.getSummary());
        employee.setProjectName(dto.getProjectName());
        employee.setTechstack(dto.getTechstack());
        if (dto.getProjectId() != null) {
            Project project = new Project();
            project.setId(Integer.valueOf(dto.getProjectId()));
            employee.setProject(project);
        }
        if (dto.getDailyUpdates() != null) {
            List<DailyUpdate> updateList = new ArrayList<>();
            for (DailyUpdateDTO d : dto.getDailyUpdates()) {
                DailyUpdate dailyUpdate = DailyUpdateMapper.toEntity(d);
                dailyUpdate.setEmployee(employee);
                updateList.add(dailyUpdate);
            }
            employee.setDailyUpdates(updateList);
        }

        return employee;
    }
    
    public static List<EmployeeDTO> toDTOList(List<Employee> employees) {
        if (employees == null) return null;

        List<EmployeeDTO> dtoList = new ArrayList<>();
        for (Employee emp : employees) {
            dtoList.add(toDTO(emp));
        }
        return dtoList;
    }

    public static List<Employee> toEntityList(List<EmployeeDTO> dtoList) {
        if (dtoList == null) return null;

        List<Employee> entityList = new ArrayList<>();
        for (EmployeeDTO dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }
 
}


