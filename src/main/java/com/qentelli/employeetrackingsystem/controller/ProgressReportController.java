package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ProgressReportDTO;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.service.ProgressReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/progress-report")
public class ProgressReportController {

	private static final Logger logger = LoggerFactory.getLogger(ProgressReportController.class);
	private final ProgressReportService service;

	@PostMapping
	public ResponseEntity<AuthResponse<Void>> create(@Valid @RequestBody ProgressReportDTO dto) {
		logger.info("Creating ProgressReport for team: {}, lead: {}", dto.getTeam(), dto.getTcbLead());
		service.create(dto);
		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.CREATED.value(), RequestProcessStatus.SUCCESS,
				"ProgressReport created successfully");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<AuthResponse<PaginatedResponse<ProgressReportDTO>>> getAll(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Page<ProgressReportDTO> dtoPage = service.getAll(page, size);
		PaginatedResponse<ProgressReportDTO> paginated = new PaginatedResponse<>(dtoPage.getContent(), page, size,
				dtoPage.getTotalElements(), dtoPage.getTotalPages(), dtoPage.isLast());
		String message = dtoPage.isEmpty() ? "No ProgressReports found" : "ProgressReports fetched successfully";
		HttpStatus status = dtoPage.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		AuthResponse<PaginatedResponse<ProgressReportDTO>> response = new AuthResponse<>(status.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), message, paginated);
		return ResponseEntity.status(status).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> update(@PathVariable Long id, @Valid @RequestBody ProgressReportDTO dto) {
		logger.info("Updating ProgressReport ID: {}", id);
		service.update(id, dto);
		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				"ProgressReport updated successfully");
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> softDelete(@PathVariable Long id) {
		logger.info("Soft deleting ProgressReport ID: {}", id);
		service.softDelete(id);
		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				"ProgressReport deleted successfully");
		return ResponseEntity.ok(response);
	}
}