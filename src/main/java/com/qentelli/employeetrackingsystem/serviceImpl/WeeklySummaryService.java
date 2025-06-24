
package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.mappers.WeeklySummaryMapper;
import com.qentelli.employeetrackingsystem.models.client.request.CreateWeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;
import com.qentelli.employeetrackingsystem.repository.EmployeeRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.WeeklySummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklySummaryService {

    private final WeeklySummaryRepository summaryRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    
    public WeeklySummaryResponse createWeeklySummary(CreateWeeklySummaryRequest request) {
        List<Project> projects = projectRepository.findAllById(request.getProjectIds());
        if (projects.size() != request.getProjectIds().size()) {
            throw new ResourceNotFoundException("Some project IDs are invalid");
        }

        List<Employee> employees = employeeRepository.findAllById(request.getEmployeeIds());
        if (employees.size() != request.getEmployeeIds().size()) {
            throw new ResourceNotFoundException("Some employee IDs are invalid");
        }

        WeeklySummary entity = WeeklySummaryMapper.toEntity(request, projects, employees);
        WeeklySummary saved = summaryRepository.save(entity);
        return WeeklySummaryMapper.toDto(saved);
    }

  

    public List<WeeklySummaryResponse> getAllSummaries() {
        return summaryRepository.findAll()
                .stream()
                .map(WeeklySummaryMapper::toDto)
                .collect(Collectors.toList());
    }
}
