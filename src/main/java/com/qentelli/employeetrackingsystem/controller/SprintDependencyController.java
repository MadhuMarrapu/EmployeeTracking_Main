package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;

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

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.SprintDependencyRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.SprintDependencyResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.SprintDependencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sprint-dependencies")
public class SprintDependencyController {

	private final SprintDependencyService sprintDependencyService;

	@PostMapping("/create")
	public ResponseEntity<AuthResponse<SprintDependencyResponse>> create(@RequestBody SprintDependencyRequest request) {
		SprintDependencyResponse created = sprintDependencyService.create(request);
		AuthResponse<SprintDependencyResponse> response = new AuthResponse<>(HttpStatus.CREATED.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Sprint dependency created successfully", created);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<AuthResponse<PaginatedResponse<SprintDependencyResponse>>> getAll(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<SprintDependencyResponse> resultPage = sprintDependencyService.getAll(pageable);
		PaginatedResponse<SprintDependencyResponse> paginated = new PaginatedResponse<>(resultPage.getContent(),
				resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalElements(), resultPage.getTotalPages(),
				resultPage.isLast());
		AuthResponse<PaginatedResponse<SprintDependencyResponse>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Sprint dependencies fetched successfully",
				paginated);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AuthResponse<SprintDependencyResponse>> getById(@PathVariable Long id) {
		SprintDependencyResponse found = sprintDependencyService.getById(id);
		AuthResponse<SprintDependencyResponse> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Sprint dependency fetched successfully", found);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<SprintDependencyResponse>> update(@PathVariable Long id,
			@RequestBody SprintDependencyRequest request) {
		SprintDependencyResponse updated = sprintDependencyService.update(id, request);
		AuthResponse<SprintDependencyResponse> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Sprint dependency updated successfully", updated);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> delete(@PathVariable Long id) {
		sprintDependencyService.delete(id);
		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Sprint dependency deleted successfully", null);
		return ResponseEntity.ok(response);
	}
}
