package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.exception.BadRequestException;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.BaseResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.request.TechStackResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ResourceRepository;

@Service
@Transactional
public class ResourceService {

	private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

	private final ResourceRepository repository;
	private final ProjectRepository projectRepository;

	public ResourceService(@Lazy ResourceRepository repository, ProjectRepository projectRepository) {
		this.repository = repository;
		this.projectRepository = projectRepository;
	}

	public ResourceResponse createResource(BaseResourceRequest dto) {
		logger.info("Creating new Resource of type: {}", dto.getResourceType());

		Resource resource = new Resource();
		populateResource(resource, dto);

		Resource saved = repository.save(resource);
		return mapToResponseDto(saved);
	}

	public ResourceResponse updateResource(Long id, BaseResourceRequest dto) {
		logger.info("Updating Resource with ID: {}", id);

		Resource resource = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));

		populateResource(resource, dto);

		Resource updated = repository.save(resource);
		return mapToResponseDto(updated);
	}

	public List<ResourceResponse> getAllResources() {
		logger.info("Fetching all resources");
		return repository.findAll().stream().map(this::mapToResponseDto).toList();
	}

	public ResourceResponse getById(Long id) {
		Resource resource = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
		return mapToResponseDto(resource);
	}

	public Page<ResourceResponse> getResourcesByTypePaginated(ResourceType type, Pageable pageable) {
		logger.info("Fetching active resources by type {} with pagination", type);
		return repository.findByResourceTypeAndResourceStatus(type, true, pageable).map(this::mapToResponseDto);
	}

	public Page<ResourceResponse> searchResourcesByTypeAndName(ResourceType type, String name, Pageable pageable) {
		if (type == null || name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Both resource type and name must be provided");
		}

		String trimmedName = name.trim();
		Page<Resource> results;

		switch (type) {
		case TECH_STACK -> {
			TechStack techStack;
			try {
				techStack = TechStack.fromString(trimmedName);
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException(
						"Invalid Tech Stack: '" + name + "'. Valid values: " + Arrays.toString(TechStack.values()));
			}
			logger.info("Filtering by TECH_STACK: {}", techStack);
			logger.debug("Query params → Type: {}, TechStack: {}, Name: {}", type, techStack, null);
			results = repository.findByResourceStatusTrueAndResourceTypeAndTechStack(type, techStack, pageable);
		}

		case PROJECT -> {
			logger.info("Filtering by PROJECT name: '{}'", trimmedName);
			logger.debug("Query params → Type: {}, TechStack: {}, Name: {}", type, null, trimmedName);
			results = repository.findByResourceStatusTrueAndResourceTypeAndProject_ProjectNameContainingIgnoreCase(type,
					trimmedName, pageable);
		}

		default -> throw new UnsupportedOperationException("Unsupported resource type: " + type);
		}

		return results.map(this::mapToResponseDto);
	}

	public void deleteResource(Long id) {
		Resource resourceFound = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
		resourceFound.setResourceStatus(false);
		repository.save(resourceFound);
		logger.info("Deleted Resource with ID: {}", id);
	}

	// 🛠️ Helper Methods

<<<<<<< HEAD
	private void populateResource(Resource resource, BaseResourceRequest dto) {
		resource.setResourceType(dto.getResourceType());

		switch (dto.getResourceType()) {
		case TECH_STACK -> {
			TechStackResourceRequest techDto = (TechStackResourceRequest) dto;
			resource.setTechStack(techDto.getTechStack());
		}
		case PROJECT -> {
			ProjectResourceRequest projectDto = (ProjectResourceRequest) dto;
			Project project = projectRepository.findById(projectDto.getProjectId()).orElseThrow(
					() -> new ResourceNotFoundException("Project not found with ID: " + projectDto.getProjectId()));
			resource.setProject(project);
		}
		default -> throw new BadRequestException("Unsupported resource type");
=======
	private void validateResourceRequest(ResourceRequest dto) {
		if (dto.getResourceType() == ResourceType.PROJECT) {
			if (dto.getProjectId() == null)
				throw new BadRequestException("Project ID is required for type PROJECT");
			if (dto.getTechStack() != null)
				throw new BadRequestException("Tech Stack must be null for type PROJECT");
		}

		if (dto.getResourceType() == ResourceType.TECH_STACK) {
			if (dto.getTechStack() == null)
				throw new BadRequestException("Tech Stack is required for type TECH_STACK");
			if (dto.getProjectId() != null)
				throw new BadRequestException("Project ID must be null for type TECH_STACK");
		}
	}

	private void populateResource(Resource resource, ResourceRequest dto) {
		resource.setResourceType(dto.getResourceType());

		// ✅ Set only the relevant field based on resourceType
		if (dto.getResourceType() == ResourceType.TECH_STACK) {
			resource.setTechStack(dto.getTechStack());
			// Do NOT set project at all
		} else if (dto.getResourceType() == ResourceType.PROJECT) {
			Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
					() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));
			resource.setProject(project);
			// Do NOT set techStack at all
>>>>>>> 084be2cd36e43c58c86c1a968b0e7344312b3106
		}

		int onsite = dto.getOnsite();
		int offsite = dto.getOffsite();
		String calculatedRatio = calculateRatio(onsite, offsite);

		resource.setOnsite(onsite);
		resource.setOffsite(offsite);
		resource.setTotalOnsiteCount(onsite);
		resource.setTotalOffsiteCount(offsite);
		resource.setRatio(calculatedRatio);
		resource.setTotalRatio(calculatedRatio);
		resource.setResourceStatus(dto.getResourceStatus());
	}

	private ResourceResponse mapToResponseDto(Resource entity) {
		Integer projectId = null;
		String projectName = null;

		if (entity.getResourceType() == ResourceType.PROJECT && entity.getProject() != null) {
			projectId = entity.getProject().getProjectId();
			projectName = entity.getProject().getProjectName();
		}

		return new ResourceResponse(entity.getResourceId(), entity.getResourceType(), entity.getTechStack(), projectId,
				projectName, entity.getOnsite(), entity.getOffsite(), entity.getTotal(), entity.getTotalOnsiteCount(),
				entity.getTotalOffsiteCount(), entity.getTotalRatio(), entity.getRatio());
	}

	private String calculateRatio(int onsite, int offsite) {
		int total = onsite + offsite;
		if (total == 0)
			return "0% : 0%";
		int onsiteRatio = (int) Math.round((onsite * 100.0) / total);
		int offsiteRatio = 100 - onsiteRatio;
		return onsiteRatio + "% : " + offsiteRatio + "%";
	}
}