package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.models.client.request.SprintDependencyRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintDependencyResponse;

public interface SprintDependencyService {

    SprintDependencyResponse create(SprintDependencyRequest request);
    Page<SprintDependencyResponse> getAll(Pageable pageable);
    SprintDependencyResponse getById(Long id);
    SprintDependencyResponse update(Long id, SprintDependencyRequest request);
    void delete(Long id); // Soft delete

    Page<SprintDependencyResponse> getBySprintId(Long sprintId, Pageable pageable);
    List<SprintDependencyResponse> getAllBySprintId(Long sprintId);

    // ✅ Lifecycle-aware fetches
    Page<SprintDependencyResponse> getBySprintIdAndStatusFlag(Long sprintId, Status statusFlag, Pageable pageable);
    List<SprintDependencyResponse> getAllBySprintIdAndStatusFlag(Long sprintId, Status statusFlag);
}