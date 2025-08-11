package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.models.client.request.SprintDependencyRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintDependencyResponse;

public interface SprintDependencyService {

	public SprintDependencyResponse create(SprintDependencyRequest request);
	public Page<SprintDependencyResponse> getAll(Pageable pageable);
	public SprintDependencyResponse getById(Long id);
	public SprintDependencyResponse update(Long id, SprintDependencyRequest request);
	public void delete(Long id);
	public Page<SprintDependencyResponse> getBySprintId(Long sprintId, Pageable pageable);
	public List<SprintDependencyResponse> getAllBySprintId(Long sprintId);
}