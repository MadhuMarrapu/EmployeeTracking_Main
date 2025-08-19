package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.SprintDependency;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
import com.qentelli.employeetrackingsystem.entity.enums.TaskStatus;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.exception.SprintNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.SprintDependencyRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintDependencyResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.SprintDependencyRepository;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;
import com.qentelli.employeetrackingsystem.service.SprintDependencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SprintDependencyServiceImpl implements SprintDependencyService {

    private final SprintDependencyRepository sprintDependencyRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;

    @Override
    public SprintDependencyResponse create(SprintDependencyRequest request) {
        log.info("Creating SprintDependency for sprintId {}: {}", request.getSprintId(), request);
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + request.getProjectId()));
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
        dependency.setStatusFlag(StatusFlag.ACTIVE);

        dependency = sprintDependencyRepository.save(dependency);
        return toResponse(dependency);
    }

    @Override
    public SprintDependencyResponse update(Long id, SprintDependencyRequest request) {
        log.info("Updating SprintDependency with ID: {}", id);
        SprintDependency dependency = sprintDependencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependency not found with ID: " + id));
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));

        BeanUtils.copyProperties(request, dependency);
        dependency.setStatusIn(TaskStatus.valueOf(request.getStatusIn()));
        dependency.setProject(project);

        dependency = sprintDependencyRepository.save(dependency);
        return toResponse(dependency);
    }

    @Override
    public void delete(Long id) {
        log.info("Soft deleting SprintDependency with ID: {}", id);
        SprintDependency dependency = sprintDependencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependency not found with ID: " + id));
        dependency.setStatusFlag(StatusFlag.INACTIVE);
        sprintDependencyRepository.save(dependency);
    }

    @Override
    public SprintDependencyResponse getById(Long id) {
        log.info("Fetching SprintDependency with ID: {}", id);
        SprintDependency entity = sprintDependencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependency not found with ID: " + id));
        return toResponse(entity);
    }

    @Override
    public Page<SprintDependencyResponse> getAll(Pageable pageable) {
        log.info("Fetching ACTIVE SprintDependencies with pagination: {}", pageable);
        return sprintDependencyRepository.findByStatusFlag(StatusFlag.ACTIVE, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<SprintDependencyResponse> getBySprintId(Long sprintId, Pageable pageable) {
        log.info("Fetching ACTIVE SprintDependencies for Sprint ID: {}", sprintId);
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException("Sprint not found with ID: " + sprintId));
        return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprint.getSprintId(), StatusFlag.ACTIVE, pageable)
                .map(this::toResponse);
    }

    @Override
    public List<SprintDependencyResponse> getAllBySprintId(Long sprintId) {
        log.info("Fetching ACTIVE SprintDependencies (non-paginated) for Sprint ID: {}", sprintId);
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException("Sprint not found with ID: " + sprintId));
        return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprint.getSprintId(), StatusFlag.ACTIVE)
                .stream().map(this::toResponse).toList();
    }
    @Override
    public Page<SprintDependencyResponse> getBySprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag, Pageable pageable) {
        log.info("Fetching SprintDependencies for Sprint ID {} with status {}", sprintId, statusFlag);
        return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprintId, statusFlag, pageable)
                .map(this::toResponse);
    }

    @Override
    public List<SprintDependencyResponse> getAllBySprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag) {
        log.info("Fetching SprintDependencies (non-paginated) for Sprint ID {} with status {}", sprintId, statusFlag);
        return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprintId, statusFlag)
                .stream().map(this::toResponse).toList();
    }

    private SprintDependencyResponse toResponse(SprintDependency entity) {
        SprintDependencyResponse response = new SprintDependencyResponse();
        BeanUtils.copyProperties(entity, response);
        response.setStatusIn(entity.getStatusIn() != null ? entity.getStatusIn().toString() : "NOT_STARTED");
        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getProjectId());
            response.setProjectName(entity.getProject().getProjectName() != null
                    ? entity.getProject().getProjectName() : "Unnamed Project");
        }
        if (entity.getSprint() != null) {
            response.setSprintId(entity.getSprint().getSprintId());
            response.setSprintName(entity.getSprint().getSprintName() != null
                    ? entity.getSprint().getSprintName() : "Unnamed Sprint");
        }
        response.setStatusFlag(entity.getStatusFlag());
        return response;
    }
}