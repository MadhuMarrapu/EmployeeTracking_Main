
package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.DailyUpdate;
import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.mappers.DailyUpdateMapper;
import com.qentelli.employeetrackingsystem.models.client.request.DailyUpdateDTO;
import com.qentelli.employeetrackingsystem.repository.DailyUpdateRepository;
import com.qentelli.employeetrackingsystem.repository.EmployeeRepository;

@Service
public class DailyUpdateService {

	private final DailyUpdateRepository dailyUpdateRepo;
	private final EmployeeRepository employeeRepo;

	public DailyUpdateService(DailyUpdateRepository dailyUpdateRepo, EmployeeRepository employeeRepo) {
		this.dailyUpdateRepo = dailyUpdateRepo;
		this.employeeRepo = employeeRepo;
	}

	public DailyUpdateDTO create(DailyUpdateDTO dto) {
		DailyUpdate entity = DailyUpdateMapper.toEntity(dto);

		Employee employee = employeeRepo.findById(dto.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		entity.setEmployee(employee);

		return DailyUpdateMapper.toDTO(dailyUpdateRepo.save(entity));
	}

	public List<DailyUpdateDTO> getAll() {
		return DailyUpdateMapper.toDTOList(dailyUpdateRepo.findAll());
	}

	public DailyUpdateDTO getById(Long id) {
		return dailyUpdateRepo.findById(id).map(DailyUpdateMapper::toDTO)
				.orElseThrow(() -> new RuntimeException("DailyUpdate not found"));
	}

	public DailyUpdateDTO update(Long id, DailyUpdateDTO dto) {
		dailyUpdateRepo.findById(id).orElseThrow(() -> new RuntimeException("DailyUpdate not found"));

		DailyUpdate entity = DailyUpdateMapper.toEntity(dto);
		entity.setId(id);

		Employee employee = employeeRepo.findById(dto.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		entity.setEmployee(employee);

		return DailyUpdateMapper.toDTO(dailyUpdateRepo.save(entity));
	}

	public void delete(Long id) {
		dailyUpdateRepo.deleteById(id);
	}

}

