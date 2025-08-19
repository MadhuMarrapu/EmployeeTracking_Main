package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.SprintDependency;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.entity.enums.TaskStatus;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.exception.SprintDependencyNotFoundException;
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

	private static final String SPRINT_DEPENDENCY_NOT_FOUND = "SprintDependency not found with ID: ";
	private static final String PROJECT_NOT_FOUND = "Project not found with ID: ";
	private static final String SPRINT_NOT_FOUND = "Sprint not found with ID: ";
	private final SprintDependencyRepository sprintDependencyRepository;
	private final ProjectRepository projectRepository;
	private final SprintRepository sprintRepository;

	@Override
	public SprintDependencyResponse create(SprintDependencyRequest request) {
		log.info("Creating SprintDependency for sprintId {}: {}", request.getSprintId(), request);
		Project project = projectRepository.findById(request.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND + request.getProjectId()));
		Sprint sprint = sprintRepository.findById(request.getSprintId())
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + request.getSprintId()));
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
		dependency.setStatusFlag(Status.ACTIVE);
		dependency = sprintDependencyRepository.save(dependency);
		return toResponse(dependency);
	}

	@Override
	public SprintDependencyResponse update(Long id, SprintDependencyRequest request) {
		log.info("Updating SprintDependency with ID: {}", id);
		SprintDependency dependency = sprintDependencyRepository.findById(id)
				.orElseThrow(() -> new SprintDependencyNotFoundException(SPRINT_DEPENDENCY_NOT_FOUND + id));
		Project project = projectRepository.findById(request.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND + request.getProjectId()));
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
				.orElseThrow(() -> new SprintDependencyNotFoundException(SPRINT_DEPENDENCY_NOT_FOUND + id));
		dependency.setStatusFlag(Status.INACTIVE);
		sprintDependencyRepository.save(dependency);
	}

	@Override
	public SprintDependencyResponse getById(Long id) {
		log.info("Fetching SprintDependency with ID: {}", id);
		SprintDependency entity = sprintDependencyRepository.findById(id)
				.orElseThrow(() -> new SprintDependencyNotFoundException(SPRINT_DEPENDENCY_NOT_FOUND + id));
		return toResponse(entity);
	}

	@Override
	public Page<SprintDependencyResponse> getAll(Pageable pageable) {
		log.info("Fetching ACTIVE SprintDependencies with pagination: {}", pageable);
		return sprintDependencyRepository.findByStatusFlag(Status.ACTIVE, pageable).map(this::toResponse);
	}

	@Override
	public Page<SprintDependencyResponse> getBySprintId(Long sprintId, Pageable pageable) {
		log.info("Fetching ACTIVE SprintDependencies for Sprint ID: {}", sprintId);
		Sprint sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + sprintId));
		return sprintDependencyRepository
				.findBySprint_SprintIdAndStatusFlag(sprint.getSprintId(), Status.ACTIVE, pageable)
				.map(this::toResponse);
	}

	@Override
	public List<SprintDependencyResponse> getAllBySprintId(Long sprintId) {
		log.info("Fetching ACTIVE SprintDependencies (non-paginated) for Sprint ID: {}", sprintId);
		Sprint sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + sprintId));
		return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprint.getSprintId(), Status.ACTIVE)
				.stream().map(this::toResponse).toList();
	}

	@Override
	public Page<SprintDependencyResponse> getBySprintIdAndStatusFlag(Long sprintId, Status statusFlag,
			Pageable pageable) {
		log.info("Fetching SprintDependencies for Sprint ID {} with status {}", sprintId, statusFlag);
		return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprintId, statusFlag, pageable)
				.map(this::toResponse);
	}

	@Override
	public List<SprintDependencyResponse> getAllBySprintIdAndStatusFlag(Long sprintId, Status statusFlag) {
		log.info("Fetching SprintDependencies (non-paginated) for Sprint ID {} with status {}", sprintId, statusFlag);
		return sprintDependencyRepository.findBySprint_SprintIdAndStatusFlag(sprintId, statusFlag).stream()
				.map(this::toResponse).toList();
	}

	private SprintDependencyResponse toResponse(SprintDependency entity) {
		SprintDependencyResponse response = new SprintDependencyResponse();
		BeanUtils.copyProperties(entity, response);
		response.setStatusIn(entity.getStatusIn() != null ? entity.getStatusIn().toString() : "NOT_STARTED");
		if (entity.getProject() != null) {
			response.setProjectId(entity.getProject().getProjectId());
			response.setProjectName(entity.getProject().getProjectName() != null ? entity.getProject().getProjectName()
					: "Unnamed Project");
		}
		if (entity.getSprint() != null) {
			response.setSprintId(entity.getSprint().getSprintId());
			response.setSprintName(
					entity.getSprint().getSprintName() != null ? entity.getSprint().getSprintName() : "Unnamed Sprint");
		}
		response.setStatusFlag(entity.getStatusFlag());
		return response;
	}
}