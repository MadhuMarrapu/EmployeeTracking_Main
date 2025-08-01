package com.qentelli.employeetrackingsystem.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.SprintDependency;
import com.qentelli.employeetrackingsystem.entity.TaskStatus;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.SprintDependencyRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintDependencyResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.SprintDependencyRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class SprintDependencyService {

	@Autowired
    private SprintDependencyRepository sprintDependencyRepository;
	
	@Autowired
    private ProjectRepository projectRepository;

    public SprintDependencyResponse create(SprintDependencyRequest request) {
        log.info("Creating SprintDependency: {}", request);

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: "+ request.getProjectId()));

        SprintDependency dependency = new SprintDependency();
        BeanUtils.copyProperties(request, dependency);
        dependency.setStatus(TaskStatus.valueOf(request.getStatus()));
        dependency.setProject(project);

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
                .orElseThrow(() -> new RuntimeException("Dependency not found"));
        return toResponse(entity);
    }

    public SprintDependencyResponse update(Long id, SprintDependencyRequest request) {
        log.info("Updating SprintDependency with ID: {}", id);

        SprintDependency dependency = sprintDependencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dependency not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: "+ request.getProjectId()));

        BeanUtils.copyProperties(request, dependency);
        dependency.setStatus(TaskStatus.valueOf(request.getStatus()));
        dependency.setProject(project);

        dependency = sprintDependencyRepository.save(dependency);
        return toResponse(dependency);
    }

    public void delete(Long id) {
        log.info("Deleting SprintDependency with ID: {}", id);
        sprintDependencyRepository.deleteById(id);
    }

    private SprintDependencyResponse toResponse(SprintDependency entity) {
        SprintDependencyResponse response = new SprintDependencyResponse();
        BeanUtils.copyProperties(entity, response);
        response.setStatus(entity.getStatus().toString());
        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getProjectId());
            response.setProjectName(entity.getProject().getProjectName());
        }
        return response;
    }
}
