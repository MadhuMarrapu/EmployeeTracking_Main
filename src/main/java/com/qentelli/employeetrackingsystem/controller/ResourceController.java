package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ListContentWrapper;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ResourceResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.ResourceService;

@RestController
@RequestMapping("/resources")
public class ResourceController {

	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
	private final ResourceService service;

	public ResourceController(@Lazy ResourceService service) {
		this.service = service;
	}

	// 🟢 Create Resource
	@PostMapping
	public ResponseEntity<AuthResponse<Void>> createResource(@RequestBody ResourceRequest dto) {
		logger.info("Received request to create resource of type: {}", dto.getResourceType());
		service.createResource(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new AuthResponse<>(201, RequestProcessStatus.SUCCESS, "Resource created successfully"));
	}

	// 🔍 Filter by Resource Type only
	@GetMapping("/filter-by-type")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> getByType(@RequestParam ResourceType type,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		logger.info("Filtering resources by type: {}, page: {}, size: {}", type, page, size);
		Pageable pageable = PageRequest.of(page, size);
		Page<ResourceResponse> results = service.getResourcesByTypePaginated(type, pageable);

		if (results.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new AuthResponse<>(404, RequestProcessStatus.FAILURE,
							"No resources found for type '" + type + "'", HttpStatus.NOT_FOUND, "Empty result set"));
		}

		PaginatedResponse<ResourceResponse> paginated = new PaginatedResponse<>(results.getContent(),
				results.getNumber(), results.getSize(), results.getTotalElements(), results.getTotalPages(),
				results.isLast());

		return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
				"Resources filtered by type: " + type, paginated));
	}

	@GetMapping("/filter-by-type-and-name")
	public ResponseEntity<AuthResponse<PaginatedResponse<ResourceResponse>>> getByTypeAndName(
			@RequestParam ResourceType type, @RequestParam String name, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		logger.info("Filtering resources by type: {}, name: {}, page: {}, size: {}", type, name, page, size);

		if (name == null || name.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new AuthResponse<>(400, RequestProcessStatus.FAILURE,
					"Search name must not be blank", HttpStatus.BAD_REQUEST, "Missing 'name' parameter"));
		}

		Pageable pageable = PageRequest.of(page, size);
		String resolvedName = name.trim();
		Page<ResourceResponse> results;

		try {
			results = service.searchResourcesByTypeAndName(type, resolvedName, pageable);
		} catch (IllegalArgumentException ex) {
			logger.warn("Validation error: {}", ex.getMessage());
			return ResponseEntity.badRequest().body(new AuthResponse<>(400, RequestProcessStatus.FAILURE,
					"Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST, "Input validation failed"));
		}

		if (results.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new AuthResponse<>(404, RequestProcessStatus.FAILURE,
							"No resources found for type '" + type + "' and name '" + resolvedName + "'",
							HttpStatus.NOT_FOUND, "No matching data"));
		}

		PaginatedResponse<ResourceResponse> paginated = new PaginatedResponse<>(results.getContent(),
				results.getNumber(), results.getSize(), results.getTotalElements(), results.getTotalPages(),
				results.isLast());

		return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
				"Found resources for type '" + type + "' and name '" + resolvedName + "'", paginated));
	}

	// 🔄 Update Resource
	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> updateResource(@PathVariable Long id, @RequestBody ResourceRequest dto) {
		logger.info("Updating resource with ID: {}, new type: {}", id, dto.getResourceType());
		service.updateResource(id, dto);
		return ResponseEntity
				.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, "Resource updated successfully"));
	}

	// 🗑️ Delete Resource
	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> deleteResource(@PathVariable Long id) {
		logger.info("Deleting resource with ID: {}", id);
		service.deleteResource(id);
		return ResponseEntity
				.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, "Resource deleted successfully"));
	}
}
