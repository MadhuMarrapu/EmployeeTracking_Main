package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.SprintDependency;
import com.qentelli.employeetrackingsystem.entity.TaskStatus;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.exception.SprintNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.SprintDependencyRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintDependencyResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.SprintDependencyRepository;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class  SprintDependencyService {

	private final SprintDependencyRepository sprintDependencyRepository;
	private final ProjectRepository projectRepository;
	private final SprintRepository sprintRepository;

	
	public SprintDependencyResponse create(SprintDependencyRequest request) {
		log.info("Creating SprintDependency for sprintId {}: {}", request.getSprintId(), request);
		Project project = projectRepository.findById(request.getProjectId()).orElseThrow(
				() -> new ProjectNotFoundException("Project not found with id: " + request.getProjectId()));
		Sprint sprint = sprintRepository.findById(request.getSprintId())
				.orElseThrow(() -> new SprintNotFoundException("Sprint not found with id: " + request.getSprintId()));
		SprintDependency dependency = new SprintDependency(); 
		dependency.setType(request.getType());
		dependency.setDescription(request.getDescription());
		dependency.setOwner(request.getOwner());
		dependency.setDate(request.getDate());
		dependency.setStatusIn(TaskStatus.valueOf(request.getStatusIn()));
		dependency.setImpact(request.getImpact());
		dependency.setActionTaken(request.getActionTaken());
		dependency.setProject(project);
		dependency.setSprint(sprint);
		dependency = sprintDependencyRepository.save(dependency);
		return toResponse(dependency);
	}

	
	public Page<SprintDependencyResponse> getAll(Pageable pageable) {
		log.info("Fetching all SprintDependencies with pagination: {}", pageable);
		return sprintDependencyRepository.findAll(pageable).map(this::toResponse);
	}

	
	public SprintDependencyResponse getById(Long id) {
		log.info("Fetching SprintDependency with ID: {}", id);
		SprintDependency entity = sprintDependencyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Dependency not found with ID: " + id));
		return toResponse(entity);
	}

	
	public SprintDependencyResponse update(Long id, SprintDependencyRequest request) {
		log.info("Updating SprintDependency with ID: {}", id);
		SprintDependency dependency = sprintDependencyRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Dependency not found with ID: " + id));
		Project project = projectRepository.findById(request.getProjectId()).orElseThrow(
				() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));
		BeanUtils.copyProperties(request, dependency);
		dependency.setStatusIn(TaskStatus.valueOf(request.getStatusIn()));
		dependency.setProject(project);
		dependency = sprintDependencyRepository.save(dependency);
		return toResponse(dependency);
	}

	
	public void delete(Long id) {
		log.info("Deleting SprintDependency with ID: {}", id);
		sprintDependencyRepository.deleteById(id);
	}
	
	public Page<SprintDependencyResponse> getBySprintId(Long sprintId, Pageable pageable) {
		log.info("Fetching SprintDependencies for Sprint ID: {}", sprintId);
		Sprint sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new SprintNotFoundException("Sprint not found with ID: " + sprintId));
		return sprintDependencyRepository.findBySprint_SprintId(sprint.getSprintId(), pageable).map(this::toResponse);
	}
  
	public List<SprintDependencyResponse> getAllBySprintId(Long sprintId) {
	    log.info("Fetching SprintDependencies (non-paginated) for Sprint ID: {}", sprintId);

	    Sprint sprint = sprintRepository.findById(sprintId)
	            .orElseThrow(() -> new SprintNotFoundException("Sprint not found with ID: " + sprintId));

	    List<SprintDependency> dependencies = sprintDependencyRepository.findBySprint_SprintId(sprintId);

	    return dependencies.stream()
	            .map(this::toResponse)
	            .collect(Collectors.toList());
	}

	private SprintDependencyResponse toResponse(SprintDependency entity) {
	    SprintDependencyResponse response = new SprintDependencyResponse();
	    BeanUtils.copyProperties(entity, response);

	    // Defensive status mapping
	    response.setStatusIn(entity.getStatusIn() != null ? entity.getStatusIn().toString() : "NOT_STARTED");

	    // Project info mapping
	    if (entity.getProject() != null) {
	        response.setProjectId(entity.getProject().getProjectId());
	        response.setProjectName(entity.getProject().getProjectName() != null 
	            ? entity.getProject().getProjectName() 
	            : "Unnamed Project");
	    }

	    // Sprint info mapping
	    if (entity.getSprint() != null) {
	        response.setSprintId(entity.getSprint().getSprintId());
	        response.setSprintName(entity.getSprint().getSprintName() != null 
	            ? entity.getSprint().getSprintName() 
	            : "Unnamed Sprint");
	    }

	    return response;
	}
}
