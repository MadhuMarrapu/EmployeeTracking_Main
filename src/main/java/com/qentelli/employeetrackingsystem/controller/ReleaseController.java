package com.qentelli.employeetrackingsystem.controller;

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ReleaseRequestDTO;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ReleaseResponseDTO;
import com.qentelli.employeetrackingsystem.serviceImpl.ReleaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
public class ReleaseController {

	private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);

	private final ReleaseService service;

	/**
	 * POST API to create a new release.
	 */
	@PostMapping("/save")
	public ResponseEntity<AuthResponse<String>> createRelease(@Valid @RequestBody ReleaseRequestDTO dto) {
		logger.info("Creating new release for project ID {}", dto.getProjectId());
		service.createRelease(dto);
		logger.info("Release created successfully");

		AuthResponse<String> response = new AuthResponse<>(HttpStatus.CREATED.value(),
				RequestProcessStatus.SUCCESS, "Release created successfully.");

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	/**
	 * GET API to fetch all releases.
	 */
	@GetMapping("/list")
	public ResponseEntity<AuthResponse<Map<String, Object>>> getAllReleases() {
		logger.info("Fetching all release entries");
		List<ReleaseResponseDTO> releaseList = service.getAllReleases();

		Map<String, Object> data = new HashMap<>();
		data.put("content", releaseList);
		data.put("totalRecords", releaseList.size());

		logger.info("Fetched {} release records", releaseList.size());

		AuthResponse<Map<String, Object>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Release data retrieved successfully", data);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * GET API to fetch a release by ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<AuthResponse<ReleaseResponseDTO>> getReleaseById(@PathVariable Long id) {
		logger.info("Fetching release with ID {}", id);
		ReleaseResponseDTO release = service.getReleaseById(id);

		AuthResponse<ReleaseResponseDTO> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Release fetched successfully", release);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * PUT API to update release data.
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<AuthResponse<String>> updateRelease(@PathVariable Long id,
			@Valid @RequestBody ReleaseRequestDTO dto) {
		logger.info("Updating release with ID {}", id);
		service.updateRelease(id, dto);
		logger.info("Release updated successfully");

		AuthResponse<String> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				"Release updated successfully.");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * DELETE API to remove a release entry.
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<AuthResponse<Void>> deleteRelease(@PathVariable Long id) {
		logger.info("Deleting release with ID {}", id);
		service.deleteRelease(id);
		logger.info("Release deleted successfully");

		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Release deleted successfully", null);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}