/*
package com.qentelli.employeetrackingsystem.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.models.client.request.CreateWeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;

public class WeeklySummaryMapper {
	
	// Prevent instantiation
    private WeeklySummaryMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static WeeklySummary toEntity(CreateWeeklySummaryRequest dto, List<Project> projects, List<Employee> employees) {
        WeeklySummary summary = new WeeklySummary();
        summary.setWeekStartDate(dto.getWeekStartDate());
        summary.setWeekEndDate(dto.getWeekEndDate());
        summary.setListProject(projects);
        summary.setEmployeeList(employees);
        return summary;
    }

    public static WeeklySummaryResponse toDto(WeeklySummary entity) {
        WeeklySummaryResponse dto = new WeeklySummaryResponse();
        dto.setId(entity.getId());
        dto.setWeekStartDate(entity.getWeekStartDate());
        dto.setWeekEndDate(entity.getWeekEndDate());
        dto.setProjectNames(
                entity.getListProject().stream()
                        .map(Project::getProjectName)
                        .collect(Collectors.toList())
        );
        dto.setEmployeeNames(
                entity.getEmployeeList().stream()
                        .map(emp -> emp.getFirstName() + " " + emp.getLastName())
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
*/