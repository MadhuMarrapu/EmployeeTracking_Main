package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.ResourceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Resource")
public class ResourceController {

	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

	private final ResourceService resourceService;

	@PostMapping
	public ResponseEntity<AuthResponse<ResourceResponse>> createResource(@Valid @RequestBody ResourceRequest request) {
		try {
			logger.info("Creating new resource of type: {}", request.getResourceType());
			ResourceResponse responseDto = resourceService.createResource(request);
			logger.debug("Resource created: {}", responseDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse<>(HttpStatus.CREATED.value(),
					RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Resource created successfully", responseDto));
		} catch (Exception ex) {
			logger.error("Error creating resource: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new AuthResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), RequestProcessStatus.FAILURE,
							"Failed to create resource", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<ResourceResponse>> updateResource(@PathVariable Long id,
			@Valid @RequestBody ResourceRequest request) {
		try {
			logger.info("Updating resource with ID: {}", id);
			ResourceResponse updated = resourceService.updateResource(id, request);
			logger.debug("Resource updated: {}", updated);
			return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
					LocalDateTime.now(), "Resource updated successfully", updated));
		} catch (Exception ex) {
			logger.error("Error updating resource: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new AuthResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), RequestProcessStatus.FAILURE,
							"Failed to update resource", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> deleteResource(@PathVariable Long id) {
		try {
			logger.info("Deleting resource with ID: {}", id);
			resourceService.deleteResource(id);
			logger.debug("Resource deleted with ID: {}", id);
			return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
					LocalDateTime.now(), "Resource deleted successfully"));
		} catch (Exception ex) {
			logger.error("Error deleting resource: {}", ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new AuthResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), RequestProcessStatus.FAILURE,
							"Failed to delete resource", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
		}
	}

	@GetMapping("/active")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> getActiveResourcesByType(
			@RequestParam(required = false) Long sprintId, @RequestParam ResourceType resourceType, Pageable pageable) {
		return buildPaginatedResponse(resourceService.getActiveResourcesByType(sprintId, resourceType, pageable),
				"Fetched active resources", "No active resources found");
	}

	@GetMapping("/search/techstack")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> searchActiveTechStack(
			@RequestParam(required = false) Long sprintId, @RequestParam ResourceType resourceType,
			@RequestParam TechStack techStack, Pageable pageable) {
		return buildPaginatedResponse(
				resourceService.searchActiveTechStack(sprintId, resourceType, techStack, pageable),
				"Fetched tech stack resources", "No tech stack resources found");
	}

	@GetMapping("/search/project")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> searchActiveProjectsByName(
			@RequestParam Long sprintId, @RequestParam ResourceType resourceType, @RequestParam String projectName,
			Pageable pageable) {
		return buildPaginatedResponse(
				resourceService.searchActiveProjectsByName(sprintId, resourceType, projectName, pageable),
				"Fetched project resources", "No project resources found");
	}

	@GetMapping("/sprint/page/all")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> getResourcesBySprintId(
			@RequestParam Long sprintId, Pageable pageable) {
		Page<ResourceResponse> page = resourceService.getAllResourcesBySprintId(sprintId, pageable);
		return buildPaginatedResponse(page, "Fetched resources for sprint ID: " + sprintId, "No resources found");
	}

	@GetMapping("/sprint/all")
	public ResponseEntity<AuthResponse<List<ResourceResponse>>> getAllResourcesBySprintId(@RequestParam Long sprintId) {
		List<ResourceResponse> allResources = resourceService.getAllResourcesBySprintId(sprintId);
		String msg = allResources.isEmpty() ? "No resources found" : "Fetched all resources for sprint ID: " + sprintId;
		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), msg, allResources));
	}

	private ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> buildPaginatedResponse(
			Page<ResourceResponse> page, String successMsg, String emptyMsg) {
		PaginatedResponse<ResourceResponse> response = new PaginatedResponse<>(page.getContent(), page.getNumber(),
				page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
		String message = page.hasContent() ? successMsg : emptyMsg;
		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), message, response));
	}

}