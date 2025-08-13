package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;
import com.qentelli.employeetrackingsystem.models.client.request.GroupedResourceResponse;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.CombinedResourceSummaryResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ProjectSummaryResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.models.client.response.TechStackSummaryResponse;

public interface ResourceService {

	public ResourceResponse createResource(ResourceRequest request);
	public ResourceResponse updateResource(Long id, ResourceRequest request);
	public void deleteResource(Long id);
	public Page<ResourceResponse> getAllResourcesBySprintId(Long sprintId, Pageable pageable);
	public Page<ResourceResponse> getActiveResourcesByType(Long sprintId, ResourceType resourceType, Pageable pageable);
	public Page<ResourceResponse> searchActiveTechStack(Long sprintId, ResourceType resourceType, TechStack techStack,
			Pageable pageable);
	public Page<ResourceResponse> searchActiveProjectsByName(Long sprintId, ResourceType resourceType,
			String projectName, Pageable pageable);
	public GroupedResourceResponse getGroupedResourcesBySprintId(Long sprintId);
	public List<ResourceResponse> getAllResourcesBySprintId(Long sprintId);
	public List<ResourceResponse> getResourcesByPreviousSprint(Long currentSprintId);	
	public List<TechStackSummaryResponse> getTechStackSummary();
	public List<ProjectSummaryResponse> getProjectSummary();
	public CombinedResourceSummaryResponse getCombinedSummary();
}