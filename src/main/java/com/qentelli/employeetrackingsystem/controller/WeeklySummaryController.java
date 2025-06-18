package com.qentelli.employeetrackingsystem.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.mappers.WeeklySummaryMapper;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.serviceImpl.WeeklySummaryServiceImpl;

@RestController
@RequestMapping("/weekly-summary")
public class WeeklySummaryController {

	private final WeeklySummaryServiceImpl service;
	private final ProjectRepository projectRepository;

	public WeeklySummaryController(WeeklySummaryServiceImpl service, ProjectRepository projectRepository) {
		this.service = service;
		this.projectRepository = projectRepository;
	}

	@PostMapping("/create")
	public WeeklySummaryResponse create(@RequestBody WeeklySummaryRequest request) {
		List<Project> projects = projectRepository.findAllById(request.getProjectIds());
		WeeklySummary entity = WeeklySummaryMapper.toEntity(request, projects);
		return WeeklySummaryMapper.toDto(service.createSummary(entity));
	}

	@PutMapping("/update/{id}")
	public WeeklySummaryResponse update(@PathVariable int id, @RequestBody WeeklySummaryRequest request) {
		List<Project> projects = projectRepository.findAllById(request.getProjectIds());
		WeeklySummary entity = WeeklySummaryMapper.toEntity(request, projects);
		return WeeklySummaryMapper.toDto(service.updateSummary(id, entity));
	}

	@GetMapping("/fetch/all")
	public List<WeeklySummaryResponse> getAll() {
		return service.getAllSummaries().stream().map(WeeklySummaryMapper::toDto).collect(Collectors.toList());
	}

	@GetMapping("/fetchById/{id}")
	public WeeklySummaryResponse getById(@PathVariable int id) {
		WeeklySummary entity = service.getSummaryById(id);
		return entity != null ? WeeklySummaryMapper.toDto(entity) : null;
	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable int id) {
		service.deleteSummary(id);
	}
}
