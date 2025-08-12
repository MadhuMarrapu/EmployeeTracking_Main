package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.service.WeekRangeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/week-ranges")
public class WeekRangeController {

	private static final Logger logger = LoggerFactory.getLogger(WeekRangeController.class);
	private final WeekRangeService service;

	@PostMapping("/save")
	public ResponseEntity<AuthResponse<String>> saveWeeklyData(@Valid @RequestBody WeekRangeRequest request) {
		logger.info("Saving weekly data from {} to {}", request.getWeekFromDate(), request.getWeekToDate());
		service.saveWeeklyData(request);
		logger.info("Weekly data saved successfully");
		AuthResponse<String> response = new AuthResponse<>(HttpStatus.CREATED.value(), RequestProcessStatus.SUCCESS,
				"Weekly data saved successfully.");

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/report")
	public ResponseEntity<AuthResponse<Map<String, Object>>> generateReport(@RequestParam LocalDate weekFromDate,
			@RequestParam LocalDate weekToDate, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		logger.info("Generating weekly report from {} to {} with page {} and size {}", weekFromDate, weekToDate, page,
				size);
		Pageable pageable = Pageable.ofSize(size).withPage(page);
		Page<WeekRangeResponse> report = service.generateReport(weekFromDate, weekToDate, pageable);
		Map<String, Object> data = new HashMap<>();
		data.put("content", report.getContent());
		data.put("pageable", Map.of("pageNumber", report.getNumber(), "pageSize", report.getSize()));
		data.put("totalElements", report.getTotalElements());
		data.put("totalPages", report.getTotalPages());
		logger.info("Weekly report generated successfully with {} records", report.getNumberOfElements());
		AuthResponse<Map<String, Object>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Weekly report fetched successfully", data);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<AuthResponse<WeekRangeResponse>> getById(@PathVariable int id) {
	    logger.info("Fetching week range with ID {}", id);
	    
	    WeekRangeResponse response = service.getById(id);

	    logger.info("Week range with ID {} fetched successfully", id);
	    return ResponseEntity.ok(
	            new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
	                    LocalDateTime.now(), "Week range fetched successfully", response)
	    );
	}


	/**
	 * DELETE API to perform soft delete for a week range.
	 */
	@DeleteMapping("/softDelete/{id}")
	public ResponseEntity<AuthResponse<Void>> softDelete(@PathVariable int id) {
		logger.info("Soft deleting week range with ID {}", id);
		service.softDelete(id);
		logger.info("Week range soft deleted successfully");

		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Week range marked as inactive successfully", null);

		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/toggle-enabled/{id}")
	public ResponseEntity<AuthResponse<Void>> toggleIsEnabled(@PathVariable int id) {
		logger.info("Toggling isEnabled for WeekRange with ID {}", id);
		boolean newStatus = service.setWeekRangeEnabled(id);
		logger.info("WeekRange with ID {} isEnabled set to {}", id, newStatus);

		String message = newStatus ? "WeekRange enabled successfully" : "WeekRange disabled successfully";
		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), message, null));
	}

}
