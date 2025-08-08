package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.qentelli.employeetrackingsystem.models.client.request.PIStandingRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PIStandingResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.service.PIStandingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pi-standing")
public class PIStandingController {

	private final PIStandingService service;

	@PostMapping
	public ResponseEntity<AuthResponse<PIStandingResponse>> create(@Valid @RequestBody PIStandingRequest dto) {
		PIStandingResponse data = service.create(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse<>(201, RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Created successfully", data));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<PIStandingResponse>> update(@PathVariable Long id,
			@Valid @RequestBody PIStandingRequest dto) {
		PIStandingResponse data = service.update(id, dto);
		return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
				"Updated successfully", data));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AuthResponse<PIStandingResponse>> get(@PathVariable Long id) {
		return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
				"Fetched successfully", service.get(id)));
	}

	@GetMapping
	public ResponseEntity<AuthResponse<PaginatedResponse<PIStandingResponse>>> list(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(wrapPage("Paged list fetched",
				service.list(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
	}

	@GetMapping("/all")
	public ResponseEntity<AuthResponse<List<PIStandingResponse>>> getAllPIStandings() {
		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "All PIStanding entries fetched successfully", service.list()));
	}

	@GetMapping("/pi/{piNumber}")
	public ResponseEntity<AuthResponse<PaginatedResponse<PIStandingResponse>>> byPi(@PathVariable int piNumber,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(wrapPage("Quarter list fetched",
				service.listByPi(piNumber, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
	}

	@GetMapping("/project/{projectId}")
	public ResponseEntity<AuthResponse<PaginatedResponse<PIStandingResponse>>> byProject(@PathVariable int projectId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(wrapPage("Project list fetched",
				service.listByProject(projectId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
				"Deleted successfully", null));
	}

	private AuthResponse<PaginatedResponse<PIStandingResponse>> wrapPage(String msg, Page<PIStandingResponse> p) {
		PaginatedResponse<PIStandingResponse> body = new PaginatedResponse<>(p.getContent(), p.getNumber(), p.getSize(),
				p.getTotalElements(), p.getTotalPages(), p.isLast());
		return new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(), msg, body);
	}
}
