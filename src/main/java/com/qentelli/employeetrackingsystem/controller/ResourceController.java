package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
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
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ResourceRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ListContentWrapper;
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
		service.createResource(dto);
		return new ResponseEntity<>(
				new AuthResponse<>(201, RequestProcessStatus.SUCCESS, "Resource created successfully"),
				HttpStatus.CREATED);
	}

	// 🔍 Filter by Resource Type (TECH_STACK or PROJECT)
	@GetMapping("/filter-by-type")
	public ResponseEntity<AuthResponse<ListContentWrapper<ResourceResponse>>> getByType(@RequestParam ResourceType type,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		logger.info("Fetching resources filtered by type: {}", type);
		Pageable pageable = PageRequest.of(page, size);
		Page<ResourceResponse> resourcesPage = service.getResourcesByTypePaginated(type, pageable);

		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Resources filtered by type: " + type,
				new ListContentWrapper<>(resourcesPage.getContent().size(), resourcesPage.getContent())));
	}

	// 🔍 Filter by Type and Name (e.g., TECH_STACK + FULLSTACK or PROJECT + INITIO)
	@GetMapping("/filter-by-type-and-name")
	public ResponseEntity<AuthResponse<ListContentWrapper<ResourceResponse>>> getByTypeAndName(
			@RequestParam ResourceType type, @RequestParam String name, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		logger.info("Fetching resources by type: {} and name: {}", type, name);
		Pageable pageable = PageRequest.of(page, size);
		Page<ResourceResponse> resourcesPage = service.searchResourcesByTypeAndName(type, name, pageable);

		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Resources filtered by type: " + type + " and name: " + name,
				new ListContentWrapper<>(resourcesPage.getContent().size(), resourcesPage.getContent())));
	}

	// 🔄 Update Resource
	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> updateResource(@PathVariable Long id, @RequestBody ResourceRequest dto) {
		service.updateResource(id, dto);
		return ResponseEntity
				.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, "Resource updated successfully"));
	}

	// 🗑️ Delete Resource
	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> deleteResource(@PathVariable Long id) {
		service.deleteResource(id);
		return ResponseEntity
				.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, "Resource deleted successfully"));
	}
}
