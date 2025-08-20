package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.CombinedResourceSummaryResponse;
import com.qentelli.employeetrackingsystem.models.client.response.GroupedResourceResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ProjectSummaryResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.models.client.response.TechStackSummaryResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ResourceRepository;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;
import com.qentelli.employeetrackingsystem.service.ResourceService;
import com.qentelli.employeetrackingsystem.service.SprintService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {
	
	private static final String RESOURCE_NOT_FOUND = "Resource not found with ID: ";
	private final ResourceRepository resourceRepository;
	private final ProjectRepository projectRepository;
	private final SprintRepository sprintRepository;
	private final SprintService sprintService;

	@Override
	@Transactional
	public ResourceResponse createResource(ResourceRequest request) {
		validateResourceRequest(request);
		Resource resource = new Resource();
		populateResourceFromRequest(resource, request);
		resource.setStatusFlag(Status.ACTIVE);
		Resource saved = resourceRepository.save(resource);
		return mapToResponse(saved);
	}

	@Override
	@Transactional
	public ResourceResponse updateResource(Long id, ResourceRequest request) {
		validateResourceRequest(request);
		Resource resource = resourceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND + id));
		populateResourceFromRequest(resource, request);
		Resource updated = resourceRepository.save(resource);
		return mapToResponse(updated);
	}

	@Override
	public void deleteResource(Long id) {
		Resource resource = resourceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND+ id));
		resource.setStatusFlag(Status.INACTIVE); 
		resourceRepository.save(resource);
	}

	@Override
	public GroupedResourceResponse getGroupedResourcesBySprintId(Long sprintId) {
		List<Resource> resources = resourceRepository.findBySprint_SprintIdAndStatusFlag(sprintId, Status.ACTIVE);
		List<ResourceResponse> techStackResources = resources.stream()
				.filter(r -> r.getResourceType() == ResourceType.TECHSTACK).map(this::mapToResponse).toList();
		List<ResourceResponse> projectResources = resources.stream()
				.filter(r -> r.getResourceType() == ResourceType.PROJECT).map(this::mapToResponse).toList();
		int techOnsite = techStackResources.stream().mapToInt(ResourceResponse::getOnsite).sum();
		int techOffsite = techStackResources.stream().mapToInt(ResourceResponse::getOffsite).sum();
		String techRatio = calculateRatio(techOnsite, techOffsite);
		int projOnsite = projectResources.stream().mapToInt(ResourceResponse::getOnsite).sum();
		int projOffsite = projectResources.stream().mapToInt(ResourceResponse::getOffsite).sum();
		String projRatio = calculateRatio(projOnsite, projOffsite);
		GroupedResourceResponse response = new GroupedResourceResponse();
		response.setTechStackResources(techStackResources);
		response.setTechStackOnsiteTotal(techOnsite);
		response.setTechStackOffsiteTotal(techOffsite);
		response.setTechStackRatio(techRatio);
		response.setProjectResources(projectResources);
		response.setProjectOnsiteTotal(projOnsite);
		response.setProjectOffsiteTotal(projOffsite);
		response.setProjectRatio(projRatio);
		return response;
	}

	@Override
	public CombinedResourceSummaryResponse getCombinedSummaryBySprint(Long sprintId) {
		List<Resource> resources = resourceRepository.findBySprint_SprintIdAndStatusFlag(sprintId, Status.ACTIVE);
		Map<TechStack, List<Resource>> techGrouped = resources.stream()
				.filter(r -> r.getResourceType() == ResourceType.TECHSTACK)
				.collect(Collectors.groupingBy(Resource::getTechStack));
		List<TechStackSummaryResponse> techStackSummary = techGrouped.entrySet().stream().map(entry -> {
			int onsite = entry.getValue().stream().mapToInt(Resource::getOnsite).sum();
			int offsite = entry.getValue().stream().mapToInt(Resource::getOffsite).sum();
			return new TechStackSummaryResponse(entry.getKey(), onsite, offsite, onsite + offsite);
		}).toList();
		Map<Project, List<Resource>> projectGrouped = resources.stream()
				.filter(r -> r.getResourceType() == ResourceType.PROJECT)
				.collect(Collectors.groupingBy(Resource::getProject));
		List<ProjectSummaryResponse> projectSummary = projectGrouped.entrySet().stream().map(entry -> {
			Project project = entry.getKey();
			int onsite = entry.getValue().stream().mapToInt(Resource::getOnsite).sum();
			int offsite = entry.getValue().stream().mapToInt(Resource::getOffsite).sum();
			return new ProjectSummaryResponse(project.getProjectId(), project.getProjectName(), onsite, offsite,
					onsite + offsite);
		}).toList();
		return new CombinedResourceSummaryResponse(techStackSummary, projectSummary);
	}

	@Override
	public Page<ResourceResponse> getAllResourcesBySprintId(Long sprintId, Pageable pageable) {
		Page<Resource> resources = resourceRepository.findBySprint_SprintIdAndStatusFlag(sprintId, Status.ACTIVE,
				pageable);
		return resources.map(this::mapToResponse);
	}

	@Override
	public List<ResourceResponse> getAllResourcesBySprintId(Long sprintId) {
		List<Resource> resources = resourceRepository.findBySprint_SprintIdAndStatusFlag(sprintId, Status.ACTIVE);
		return resources.stream().map(this::mapToResponse).toList();
	}

	@Override
	public Page<ResourceResponse> getPaginatedResourcesByPreviousSprint(Long currentSprintId, Pageable pageable) {
		Sprint previousSprint = sprintService.getPreviousSprint(currentSprintId);
		Page<Resource> resourcePage = resourceRepository
				.findBySprint_SprintIdAndStatusFlag(previousSprint.getSprintId(), Status.ACTIVE, pageable);
		return resourcePage.map(this::mapToResponse);
	}

	@Override
	public Page<ResourceResponse> getActiveResourcesByType(Long sprintId, ResourceType resourceType,
			Pageable pageable) {
		Page<Resource> resources;
		if (sprintId != null) {
			resources = resourceRepository.findBySprint_SprintIdAndResourceTypeAndStatusFlag(sprintId, resourceType,
					Status.ACTIVE, pageable);
		} else {
			resources = resourceRepository.findByResourceTypeAndStatusFlag(resourceType, Status.ACTIVE, pageable);
		}
		return resources.map(this::mapToResponse);
	}

	@Override
	public Page<ResourceResponse> searchActiveTechStack(Long sprintId, ResourceType resourceType, TechStack techStack,
			Pageable pageable) {
		Page<Resource> resources;
		if (sprintId != null) {
			resources = resourceRepository.findBySprint_SprintIdAndResourceTypeAndTechStackAndStatusFlag(sprintId,
					resourceType, techStack, Status.ACTIVE, pageable);
		} else {
			resources = resourceRepository.findByStatusFlagAndResourceTypeAndTechStack(Status.ACTIVE, resourceType,
					techStack, pageable);
		}
		return resources.map(this::mapToResponse);
	}

	@Override
	public Page<ResourceResponse> searchActiveProjectsByName(Long sprintId, ResourceType resourceType,
			String projectName, Pageable pageable) {
		Page<Resource> resources = resourceRepository
				.findBySprint_SprintIdAndResourceTypeAndProject_ProjectNameContainingIgnoreCaseAndStatusFlag(sprintId,
						resourceType, projectName, Status.ACTIVE, pageable);
		return resources.map(this::mapToResponse);
	}

	private void validateResourceRequest(ResourceRequest request) {
		ResourceType type = request.getResourceType();
		if (type == ResourceType.TECHSTACK) {
			if (request.getTechStack() == null) {
				throw new IllegalArgumentException("TechStack must be provided for TECHSTACK resource type");
			}
			if (request.getProjectId() != null) {
				throw new IllegalArgumentException("Project must not be provided for TECHSTACK resource type");
			}
		} else if (type == ResourceType.PROJECT) {
			if (request.getProjectId() == null) {
				throw new IllegalArgumentException("Project must be provided for PROJECT resource type");
			}
			if (request.getTechStack() != null) {
				throw new IllegalArgumentException("TechStack must not be provided for PROJECT resource type");
			}
		} else {
			throw new IllegalArgumentException("Unsupported resource type: " + type);
		}
		if (request.getSprintId() == null) {
			throw new IllegalArgumentException("Sprint ID must be provided for all resource types");
		}
	}

	private void populateResourceFromRequest(Resource resource, ResourceRequest request) {
		resource.setResourceType(request.getResourceType());
		resource.setOnsite(request.getOnsite());
		resource.setOffsite(request.getOffsite());
		resource.setRatio(calculateRatio(request.getOnsite(), request.getOffsite()));
		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + request.getProjectId()));
			resource.setProject(project);
		}
		if (request.getTechStack() != null) {
			resource.setTechStack(request.getTechStack());
		}
		Sprint sprint = sprintRepository.findById(request.getSprintId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid sprint ID: " + request.getSprintId()));
		resource.setSprint(sprint);
		resource.enforceTypeConstraints();
		log.debug("Prepared resource for persistence: type={}, techStack={}, project={}", resource.getResourceType(),
				resource.getTechStack(), resource.getProject() != null ? resource.getProject().getProjectId() : null);
	}

	private ResourceResponse mapToResponse(Resource resource) {
		ResourceResponse response = new ResourceResponse();
		response.setResourceId(resource.getResourceId());
		response.setResourceType(resource.getResourceType());
		response.setTechStack(resource.getTechStack());
		if (resource.getProject() != null) {
			response.setProjectId(resource.getProject().getProjectId());
			response.setProjectName(resource.getProject().getProjectName());
		}
		if (resource.getSprint() != null) {
			response.setSprintId(resource.getSprint().getSprintId());
			response.setSprintName(resource.getSprint().getSprintName());
		}
		response.setOnsite(resource.getOnsite());
		response.setOffsite(resource.getOffsite());
		response.setTotal(resource.getTotal());
		response.setRatio(resource.getRatio());
		response.setStatusFlag(resource.getStatusFlag());
		return response;
	}

	private String calculateRatio(int onsite, int offsite) {
		int total = onsite + offsite;
		if (total == 0)
			return "0% : 0%";
		int onsitePercent = (int) ((onsite * 100.0) / total);
		int offsitePercent = 100 - onsitePercent;
		return onsitePercent + "% : " + offsitePercent + "%";
	}

	@Override
	public Page<ResourceResponse> getResourcesBySprintIdAndStatusFlag(Long sprintId, Status statusFlag,
			Pageable pageable) {
		Page<Resource> resources = resourceRepository.findBySprint_SprintIdAndStatusFlag(sprintId, statusFlag,
				pageable);
		return resources.map(this::mapToResponse);
	}

	@Override
	public List<ResourceResponse> getResourcesBySprintIdAndStatusFlag(Long sprintId, Status statusFlag) {
		List<Resource> resources = resourceRepository.findBySprint_SprintIdAndStatusFlag(sprintId, statusFlag);
		return resources.stream().map(this::mapToResponse).toList();
	}

	@Override
	public Page<ResourceResponse> getResourcesByTypeAndStatusFlag(Long sprintId, ResourceType resourceType,
			Status statusFlag, Pageable pageable) {
		if (sprintId != null) {
			return resourceRepository
					.findBySprint_SprintIdAndResourceTypeAndStatusFlag(sprintId, resourceType, statusFlag, pageable)
					.map(this::mapToResponse);
		} else {
			return resourceRepository.findByResourceTypeAndStatusFlag(resourceType, statusFlag, pageable)
					.map(this::mapToResponse);
		}
	}

	@Override
	public Page<ResourceResponse> searchResourcesByTechStack(Long sprintId, ResourceType resourceType,
			TechStack techStack, Status statusFlag, Pageable pageable) {
		if (sprintId != null) {
			return resourceRepository.findBySprint_SprintIdAndResourceTypeAndTechStackAndStatusFlag(sprintId,
					resourceType, techStack, statusFlag, pageable).map(this::mapToResponse);
		} else {
			return resourceRepository
					.findByStatusFlagAndResourceTypeAndTechStack(statusFlag, resourceType, techStack, pageable)
					.map(this::mapToResponse);
		}
	}

	@Override
	public Page<ResourceResponse> searchResourcesByProjectName(Long sprintId, ResourceType resourceType,
			String projectName, Status statusFlag, Pageable pageable) {
		return resourceRepository
				.findBySprint_SprintIdAndResourceTypeAndProject_ProjectNameContainingIgnoreCaseAndStatusFlag(sprintId,
						resourceType, projectName, statusFlag, pageable)
				.map(this::mapToResponse);
	}

}