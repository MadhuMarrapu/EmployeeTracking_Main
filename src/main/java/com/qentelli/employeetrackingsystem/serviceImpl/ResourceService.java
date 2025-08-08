package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Resource;
import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ResourceRepository;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;

   



    @Transactional
    public ResourceResponse createResource(ResourceRequest request) {
        validateResourceRequest(request);
        Resource resource = new Resource();
        populateResourceFromRequest(resource, request);
        Resource saved = resourceRepository.save(resource);
        log.info("Resource created with ID: {}", saved.getResourceId());
        return mapToResponse(saved);
    }

    @Transactional
    public ResourceResponse updateResource(Long id, ResourceRequest request) {
        validateResourceRequest(request);
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
        populateResourceFromRequest(resource, request);
        Resource updated = resourceRepository.save(resource);
        return mapToResponse(updated);
    }

    public void deleteResource(Long id) {
		Resource resource = resourceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));
		resource.setResourceStatus(false);
		resourceRepository.save(resource);
	}
    
    public Page<ResourceResponse> getActiveResourcesByType(Long sprintId, ResourceType resourceType,
			Pageable pageable) {
		Page<Resource> resources;

		if (sprintId != null) {
			resources = resourceRepository.findBySprint_SprintIdAndResourceTypeAndResourceStatusTrue(sprintId,
					resourceType, pageable);
		} else {
			resources = resourceRepository.findByResourceTypeAndResourceStatus(resourceType, true, pageable);
		}

		return resources.map(this::mapToResponse);
	}

	public Page<ResourceResponse> searchActiveTechStack(Long sprintId, ResourceType resourceType, TechStack techStack,
			Pageable pageable) {
		Page<Resource> resources;
		if (sprintId != null) {
			resources = resourceRepository.findBySprint_SprintIdAndResourceTypeAndTechStackAndResourceStatusTrue(
					sprintId, resourceType, techStack, pageable);
		} else {
			resources = resourceRepository.findByResourceStatusTrueAndResourceTypeAndTechStack(resourceType, techStack,
					pageable);
		}

		return resources.map(this::mapToResponse);
	}

	public Page<ResourceResponse> searchActiveProjectsByName(Long sprintId, ResourceType resourceType,
			String projectName, Pageable pageable) {
		Page<Resource> resources = resourceRepository
				.findBySprint_SprintIdAndResourceTypeAndProject_ProjectNameContainingIgnoreCaseAndResourceStatusTrue(
						sprintId, resourceType, projectName, pageable);
		return resources.map(this::mapToResponse);
	}

    // ✅ Validation based on resourceType
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
        resource.setTotalRatio(resource.getRatio());
        resource.setTotalOnsiteCount(request.getOnsite());
        resource.setTotalOffsiteCount(request.getOffsite());
        resource.setResourceStatus(request.getResourceStatus());

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

        // ✅ Enforce nullability constraints AFTER all assignments
        resource.enforceTypeConstraints();

        log.debug("Prepared resource for persistence: type={}, techStack={}, project={}",
                resource.getResourceType(), resource.getTechStack(),
                resource.getProject() != null ? resource.getProject().getProjectId() : null);
    }
    // 📦 Helper: Map entity to response DTO
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
        response.setTotalOnsiteCount(resource.getTotalOnsiteCount());
        response.setTotalOffsiteCount(resource.getTotalOffsiteCount());
        response.setTotalRatio(resource.getTotalRatio());
        response.setRatio(resource.getRatio());

        return response;
    }

    // 📊 Helper: Calculate onsite/offsite ratio
    private String calculateRatio(int onsite, int offsite) {
        int total = onsite + offsite;
        if (total == 0) return "0% : 0%";
        int onsitePercent = (int) ((onsite * 100.0) / total);
        int offsitePercent = 100 - onsitePercent;
        return onsitePercent + "% : " + offsitePercent + "%";
    }
}