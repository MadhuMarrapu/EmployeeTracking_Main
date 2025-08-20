package com.qentelli.employeetrackingsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.models.client.request.SprintRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintResponse;

public interface SprintService {

	SprintResponse createSprint(SprintRequest request);

    //SprintResponse createSprint1(SprintRequest request);

    Page<SprintResponse> getAllSprints(Pageable pageable);

    SprintResponse getSprintById(Long id);

    SprintResponse updateSprint(Long id, SprintRequest request);

    void deleteSprint(Long id);

    EnableStatus toggleSprintEnabled(Long sprintId); // ✅ replaces boolean flag

    Sprint getPreviousSprint(Long sprintId);

}