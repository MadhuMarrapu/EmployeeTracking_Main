package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;
import com.qentelli.employeetrackingsystem.models.client.request.GroupedResourceResponse;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.CombinedResourceSummaryResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;

public interface ResourceService {

    ResourceResponse createResource(ResourceRequest request);
    ResourceResponse updateResource(Long id, ResourceRequest request);
    void deleteResource(Long id); // Soft delete: sets statusFlag = INACTIVE

    // ✅ Lifecycle-aware fetches
    Page<ResourceResponse> getResourcesBySprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag, Pageable pageable);
    List<ResourceResponse> getResourcesBySprintIdAndStatusFlag(Long sprintId, StatusFlag statusFlag);

    // ✅ Type-based filtering
    Page<ResourceResponse> getResourcesByTypeAndStatusFlag(Long sprintId, ResourceType resourceType, StatusFlag statusFlag, Pageable pageable);

    // ✅ Tech stack filtering
    Page<ResourceResponse> searchResourcesByTechStack(Long sprintId, ResourceType resourceType, TechStack techStack, StatusFlag statusFlag, Pageable pageable);

    // ✅ Project name filtering
    Page<ResourceResponse> searchResourcesByProjectName(Long sprintId, ResourceType resourceType, String projectName, StatusFlag statusFlag, Pageable pageable);

    // ✅ Grouping and summaries
    GroupedResourceResponse getGroupedResourcesBySprintId(Long sprintId);
    CombinedResourceSummaryResponse getCombinedSummaryBySprint(Long sprintId);

    // ✅ Previous sprint support
    Page<ResourceResponse> getPaginatedResourcesByPreviousSprint(Long currentSprintId, Pageable pageable);
    
    public Page<ResourceResponse> getAllResourcesBySprintId(Long sprintId, Pageable pageable);
    
    public List<ResourceResponse> getAllResourcesBySprintId(Long sprintId);
    public Page<ResourceResponse> getActiveResourcesByType(Long sprintId, ResourceType resourceType, Pageable pageable);
    public Page<ResourceResponse> searchActiveTechStack(Long sprintId, ResourceType resourceType, TechStack techStack,
	        Pageable pageable);
  
	public Page<ResourceResponse> searchActiveProjectsByName(Long sprintId, ResourceType resourceType,
	        String projectName, Pageable pageable);
    }