package com.qentelli.employeetrackingsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.models.client.request.SprintRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintResponse;

public interface SprintService {

	public SprintResponse createSprint(SprintRequest request);
	public SprintResponse createSprint1(SprintRequest request);
	public Page<SprintResponse> getAllSprints(Pageable pageable);
	public SprintResponse getSprintById(Long id);
	public SprintResponse updateSprint(Long id, SprintRequest request);
	public void deleteSprint(Long id);
	public boolean setSprintEnabled(Long sprintId);
	public Sprint getPreviousSprint(Long sprintId);
}