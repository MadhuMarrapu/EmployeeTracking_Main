package com.qentelli.employeetrackingsystem.serviceImpl;

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
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
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

    // 🟢 Create
    public ResourceResponse createResource(ResourceRequest dto) {
        logger.info("Creating new Resource with type: {}", dto.getResourceType());

        Resource resource = new Resource();
        resource.setResourceType(dto.getResourceType());

        if (dto.getResourceType() == ResourceType.TECH_STACK) {
            resource.setTechStack(dto.getTechStack());
            resource.setProject(null); // No project set
        } else if (dto.getResourceType() == ResourceType.PROJECT) {
            if (dto.getProjectId() == null) {
                throw new IllegalArgumentException("Project ID must be provided for resource type: PROJECT");
            }

            Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));
            resource.setProject(project);
            resource.setTechStack(null); // TechStack not relevant
        } else {
            throw new IllegalArgumentException("Unsupported resource type: " + dto.getResourceType());
        }

        // Apply ratio logic
        resource.setOnsite(dto.getOnsite());
        resource.setOffsite(dto.getOffsite());
        resource.setRatio(calculateRatio(dto.getOnsite(), dto.getOffsite()));

        Resource saved = repository.save(resource);
        logger.info("Resource saved with ID: {}", saved.getResourceId());

        return mapToResponseDto(saved);
    }
    // 📦 Get All
    public List<ResourceResponse> getAllResources() {
        logger.info("Fetching all Resource entries");
        return repository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // 📄 Paginated Read
    public Page<ResourceResponse> getAllResourcesPaginated(Pageable pageable) {
        logger.info("Fetching resources with pageable: {}", pageable);
        return repository.findAll(pageable).map(this::mapToResponseDto);
    }

    // 🔍 Get by ID
    public ResourceResponse getById(Long id) {
        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
        return mapToResponseDto(resource);
    }

    // 🔍 Get by ResourceType
    public List<ResourceResponse> getResourcesByType(ResourceType type) {
        logger.info("Fetching resources filtered by type: {}", type);
        return repository.findByResourceType(type)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // 🔍 Get by ResourceType and Name
    public List<ResourceResponse> getResourcesByTypeAndName(ResourceType type, String name) {
        logger.info("Searching resources by type: {} and name: {}", type, name);

        String processedName = type == ResourceType.TECH_STACK && name != null
                ? name.trim().toUpperCase()
                : name;

        List<Resource> resources = repository.findByResourceTypeAndName(type, processedName);

        if (resources.isEmpty()) {
            logger.warn("No resources found for type '{}' with name '{}'", type, name);
            throw new ResourceNotFoundException("No resources found for type '" + type + "' and name '" + name + "'");
        }

        return resources.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // 🔄 Update
    public ResourceResponse updateResource(Long id, ResourceRequest dto) {
        logger.info("Updating Resource with ID: {}", id);

        Resource resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));

        resource.setResourceType(dto.getResourceType());

        if (dto.getResourceType() == ResourceType.TECH_STACK) {
            resource.setTechStack(dto.getTechStack());
            resource.setProject(null); // clear project if switching to TECH_STACK
        } else if (dto.getResourceType() == ResourceType.PROJECT) {
            if (dto.getProjectId() == null) {
                throw new IllegalArgumentException("Project ID must be provided for resource type: PROJECT");
            }

            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));
            resource.setProject(project);
            resource.setTechStack(null); // clear techStack if switching to PROJECT
        }

        resource.setOnsite(dto.getOnsite());
        resource.setOffsite(dto.getOffsite());
        resource.setRatio(calculateRatio(dto.getOnsite(), dto.getOffsite()));

        Resource updated = repository.save(resource);
        logger.info("Resource updated successfully with ID: {}", updated.getResourceId());

        return mapToResponseDto(updated);
    }

    // 🗑️ Delete
    public void deleteResource(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Resource not found with ID: " + id);
        }
        repository.deleteById(id);
        logger.info("Resource deleted with ID: {}", id);
    }

    // 🧮 Ratio Calculation
    private String calculateRatio(int onsite, int offsite) {
        int total = onsite + offsite;
        if (total == 0) return "0% : 0%";
        int onsiteRatio = (int) Math.round((onsite * 100.0) / total);
        int offsiteRatio = 100 - onsiteRatio;
        return onsiteRatio + "% : " + offsiteRatio + "%";
    }

    // 🔄 DTO Mapper
    private ResourceResponse mapToResponseDto(Resource entity) {
        ResourceType type = entity.getResourceType();

        Integer projectId = null;
        String projectName = null;
        TechStack techStack = null;

        if (type == ResourceType.PROJECT && entity.getProject() != null) {
            projectId = entity.getProject().getProjectId();
            projectName = entity.getProject().getProjectName();
        }

        if (type == ResourceType.TECH_STACK) {
            techStack = entity.getTechStack();
        }

        return new ResourceResponse(
        	    entity.getResourceId(),
        	    type,
        	    techStack,
        	    projectId != null ? projectId : null,  
        	    projectName,
        	    entity.getOnsite(),
        	    entity.getOffsite(),
        	    entity.getTotal(),
        	    entity.getTotalOnsiteCount(),
        	    entity.getTotalOffsiteCount(),
        	    entity.getTotalRatio(),
        	    entity.getRatio()
        	);
    }
}
