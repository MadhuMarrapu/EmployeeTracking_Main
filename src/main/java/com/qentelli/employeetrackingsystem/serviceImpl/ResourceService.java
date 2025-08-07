package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ResourceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceService {

	private final ResourceRepository resourceRepository;
	private final ProjectRepository projectRepository;

	// 🔹 Create
	public ResourceResponse createResource(ResourceRequest request) {
		validateRequest(request);
		Resource resource = new Resource();
		resource.setResourceType(request.getResourceType());
		resource.setOnsite(request.getOnsite());
		resource.setOffsite(request.getOffsite());
		resource.setResourceStatus(request.getResourceStatus());

		if (request.getResourceType() == ResourceType.TECH_STACK && request.getTechStack() != null) {
			resource.setTechStack(request.getTechStack());
		}

		if (request.getResourceType() == ResourceType.PROJECT && request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + request.getProjectId()));
			resource.setProject(project);
		}

		updateRatios(resource);
		Resource saved = resourceRepository.save(resource);
		return mapToResponse(saved);
	}

	// 🔹 Read All
	public List<ResourceResponse> getAllResources() {
		return resourceRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	// 🔹 Read by ID
	public ResourceResponse getResourceById(Long id) {
		Resource resource = resourceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Resource not found with ID: " + id));
		return mapToResponse(resource);
	}

	// 🔹 Update
	public ResourceResponse updateResource(Long id, ResourceRequest request) {
		validateRequest(request);
		Resource existing = resourceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Resource not found with ID: " + id));

		existing.setResourceType(request.getResourceType());
		existing.setOnsite(request.getOnsite());
		existing.setOffsite(request.getOffsite());
		existing.setResourceStatus(request.getResourceStatus());

		if (request.getResourceType() == ResourceType.TECH_STACK && request.getTechStack() != null) {
			existing.setTechStack(request.getTechStack());
			// Do not touch project
		}

		if (request.getResourceType() == ResourceType.PROJECT && request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + request.getProjectId()));
			existing.setProject(project);
			// Do not touch techStack
		}

		updateRatios(existing);
		Resource updated = resourceRepository.save(existing);
		return mapToResponse(updated);
	}

	// 🔹 Delete
	public void deleteResource(Long id) {
		Resource resource = resourceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Resource not found with ID: " + id));
		resourceRepository.delete(resource);
	}
	
	public Page<ResourceResponse> getActiveResourcesByType(ResourceType resourceType, Pageable pageable) {
	    Page<Resource> resources = resourceRepository
	            .findByResourceTypeAndResourceStatus(resourceType, true, pageable);
	    return resources.map(this::mapToResponse);
	}
	
	public Page<ResourceResponse> searchActiveTechStack(ResourceType resourceType, TechStack techStack, Pageable pageable) {
	    Page<Resource> resources = resourceRepository
	            .findByResourceStatusTrueAndResourceTypeAndTechStack(resourceType, techStack, pageable);
	    return resources.map(this::mapToResponse);
	}
	
	public Page<ResourceResponse> searchActiveProjectsByName(ResourceType resourceType, String projectName, Pageable pageable) {
	    Page<Resource> resources = resourceRepository
	            .findByResourceStatusTrueAndResourceTypeAndProject_ProjectNameContainingIgnoreCase(resourceType, projectName, pageable);
	    return resources.map(this::mapToResponse);
	}


	// 🔸 Validation
	private void validateRequest(ResourceRequest request) {
		if (request.getResourceType() == ResourceType.TECH_STACK && request.getProjectId() != null) {
			throw new IllegalArgumentException("TECH_STACK must not have projectId");
		}
		if (request.getResourceType() == ResourceType.PROJECT && request.getTechStack() != null) {
			throw new IllegalArgumentException("PROJECT must not have techStack");
		}
	}

	// 🔸 Entity → DTO
	private ResourceResponse mapToResponse(Resource resource) {
		ResourceResponse response = new ResourceResponse();
		response.setResourceId(resource.getResourceId());
		response.setResourceType(resource.getResourceType());

		if (resource.getResourceType() == ResourceType.TECH_STACK && resource.getTechStack() != null) {
			response.setTechStack(resource.getTechStack());
		}

		if (resource.getResourceType() == ResourceType.PROJECT && resource.getProject() != null) {
			response.setProjectId(resource.getProject().getProjectId());
			response.setProjectName(resource.getProject().getProjectName());
		}

		response.setOnsite(resource.getOnsite());
		response.setOffsite(resource.getOffsite());
		response.setTotal(resource.getTotal());
		response.setTotalOnsiteCount(resource.getTotalOnsiteCount());
		response.setTotalOffsiteCount(resource.getTotalOffsiteCount());
		response.setRatio(resource.getRatio());
		response.setTotalRatio(resource.getTotalRatio());

		return response;
	}

	// 🔸 Ratio Calculation
	private void updateRatios(Resource resource) {
		int onsite = resource.getOnsite();
		int offsite = resource.getOffsite();
		int total = onsite + offsite;

		resource.setTotalOnsiteCount(onsite);
		resource.setTotalOffsiteCount(offsite);

		String ratio = total > 0
				? String.format("%d%% : %d%%", Math.round((onsite * 100.0) / total),
						Math.round((offsite * 100.0) / total))
				: "0% : 0%";

		resource.setRatio(ratio);
		resource.setTotalRatio(ratio);
	}
}