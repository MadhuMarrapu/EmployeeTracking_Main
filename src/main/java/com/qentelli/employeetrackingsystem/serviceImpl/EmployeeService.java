/*
package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.mappers.EmployeeMapper;
import com.qentelli.employeetrackingsystem.models.client.request.EmployeeDTO;
import com.qentelli.employeetrackingsystem.repository.EmployeeRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepo;
	private final ProjectRepository projectRepo;

	public EmployeeService(EmployeeRepository employeeRepo, ProjectRepository projectRepo) {
		this.employeeRepo = employeeRepo;
		this.projectRepo = projectRepo;
	}

	public EmployeeDTO create(EmployeeDTO dto) {
		Employee employee = EmployeeMapper.toEntity(dto);
		employee.setProject(projectRepo.findById(Integer.valueOf(dto.getProjectId()))
				.orElseThrow(() -> new RuntimeException("Project not found")));
		return EmployeeMapper.toDTO(employeeRepo.save(employee));
	}

	public List<EmployeeDTO> getAll() {
		return EmployeeMapper.toDTOList(employeeRepo.findAll());
	}

	public EmployeeDTO getById(String id) {
		return employeeRepo.findById(id).map(EmployeeMapper::toDTO)
				.orElseThrow(() -> new RuntimeException("Employee not found"));
	}

	public EmployeeDTO update(String id, EmployeeDTO dto) {
		employeeRepo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

		Employee employee = EmployeeMapper.toEntity(dto);
		employee.setEmployeeId(id); // ensure ID consistency
		employee.setProject(projectRepo.findById(Integer.valueOf(dto.getProjectId()))
				.orElseThrow(() -> new RuntimeException("Project not found")));
		return EmployeeMapper.toDTO(employeeRepo.save(employee));
	}

	public void delete(String id) {
		employeeRepo.deleteById(id);
	}

}

*/
