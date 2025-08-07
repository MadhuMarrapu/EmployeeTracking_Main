package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		logger.info("Creating resource of type: {}", request.getResourceType());
		ResourceResponse created = resourceService.createResource(request);

		AuthResponse<ResourceResponse> response = new AuthResponse<>(HttpStatus.CREATED.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Resource created successfully", created);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<AuthResponse<List<ResourceResponse>>> getAllResources() {
		logger.info("Fetching all resources");
		List<ResourceResponse> resources = resourceService.getAllResources();

		AuthResponse<List<ResourceResponse>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Resources fetched successfully", resources);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AuthResponse<ResourceResponse>> getResourceById(@PathVariable Long id) {
		logger.info("Fetching resource with ID: {}", id);
		ResourceResponse resource = resourceService.getResourceById(id);

		AuthResponse<ResourceResponse> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Resource fetched successfully", resource);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/type/{resourceType}/active")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> getActiveResourcesByType(
	        @PathVariable ResourceType resourceType,
	        @RequestParam(required = false) Long sprintId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<ResourceResponse> pageData = resourceService.getActiveResourcesByType(sprintId, resourceType, pageable);

	    PaginatedResponse<ResourceResponse> paginated = new PaginatedResponse<>(pageData.getContent(), page, size,
	            pageData.getTotalElements(), pageData.getTotalPages(), pageData.isLast());

	    String message = pageData.isEmpty()
	            ? "No active resources found" + (sprintId != null ? " for sprint " + sprintId : "")
	            : "Active resources fetched successfully" + (sprintId != null ? " for sprint " + sprintId : "");

	    HttpStatus status = pageData.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

	    return ResponseEntity.status(status).body(new AuthResponse<>(status.value(), RequestProcessStatus.SUCCESS,
	            LocalDateTime.now(), message, paginated));
	}

	@GetMapping("/search/techstack")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> searchTechStackResources(
	        @RequestParam ResourceType resourceType,
	        @RequestParam TechStack techStack,
	        @RequestParam(required = false) Long sprintId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<ResourceResponse> pageData = resourceService.searchActiveTechStack(sprintId, resourceType, techStack, pageable);

	    PaginatedResponse<ResourceResponse> paginated = new PaginatedResponse<>(pageData.getContent(), page, size,
	            pageData.getTotalElements(), pageData.getTotalPages(), pageData.isLast());

	    String message = pageData.isEmpty()
	            ? "No TECH_STACK resources found" + (sprintId != null ? " for sprint " + sprintId : "")
	            : "TECH_STACK resources fetched successfully" + (sprintId != null ? " for sprint " + sprintId : "");

	    HttpStatus status = pageData.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

	    return ResponseEntity.status(status).body(new AuthResponse<>(status.value(), RequestProcessStatus.SUCCESS,
	            LocalDateTime.now(), message, paginated));
	}

	@GetMapping("/search/project")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> searchProjectResources(
	        @RequestParam ResourceType resourceType,
	        @RequestParam String projectName,
	        @RequestParam(required = false) Long sprintId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<ResourceResponse> pageData = resourceService.searchActiveProjectsByName(sprintId, resourceType, projectName, pageable);

	    PaginatedResponse<ResourceResponse> paginated = new PaginatedResponse<>(pageData.getContent(), page, size,
	            pageData.getTotalElements(), pageData.getTotalPages(), pageData.isLast());

	    String message = pageData.isEmpty()
	            ? "No PROJECT resources found" + (sprintId != null ? " for sprint " + sprintId : "")
	            : "PROJECT resources fetched successfully" + (sprintId != null ? " for sprint " + sprintId : "");

	    HttpStatus status = pageData.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

	    return ResponseEntity.status(status).body(new AuthResponse<>(status.value(), RequestProcessStatus.SUCCESS,
	            LocalDateTime.now(), message, paginated));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<ResourceResponse>> updateResource(@PathVariable Long id,
			@Valid @RequestBody ResourceRequest request) {
		logger.info("Updating resource with ID: {}", id);
		ResourceResponse updated = resourceService.updateResource(id, request);

		AuthResponse<ResourceResponse> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Resource updated successfully", updated);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> deleteResource(@PathVariable Long id) {
		logger.info("Deleting resource with ID: {}", id);
		resourceService.deleteResource(id);

		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Resource deleted successfully");

		return ResponseEntity.ok(response);
	}
}