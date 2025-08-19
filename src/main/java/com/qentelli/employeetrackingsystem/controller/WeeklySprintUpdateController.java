package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySprintUpdateDto;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ListContentWrapper;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.service.WeeklySprintUpdateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weekly-sprint-update")
public class WeeklySprintUpdateController {

	private static final Logger logger = LoggerFactory.getLogger(WeeklySprintUpdateController.class);

	private final WeeklySprintUpdateService service;

	@PostMapping
	public ResponseEntity<AuthResponse<WeeklySprintUpdateDto>> create(@Valid @RequestBody WeeklySprintUpdateDto dto) {
		logger.info("Creating WeeklySprintUpdate for projectId={}, weekRangeId={}", dto.getProjectId(),
				dto.getWeeekRangeId());
		WeeklySprintUpdateDto createdDto = service.createUpdate(dto);
		AuthResponse<WeeklySprintUpdateDto> response = new AuthResponse<>(HttpStatus.CREATED.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Weekly sprint update created successfully",
				createdDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<AuthResponse<PaginatedResponse<WeeklySprintUpdateDto>>> getAllActivePaginated(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "weekSprintId") String sortBy) {
		logger.info("Fetching active WeeklySprintUpdates: page={}, size={}, sortBy={}", page, size, sortBy);
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy.trim()));
		Page<WeeklySprintUpdateDto> updatePage = service.getAllUpdates(pageable);
		PaginatedResponse<WeeklySprintUpdateDto> paginated = new PaginatedResponse<>(updatePage.getContent(),
				updatePage.getNumber(), updatePage.getSize(), updatePage.getTotalElements(), updatePage.getTotalPages(),
				updatePage.isLast());
		AuthResponse<PaginatedResponse<WeeklySprintUpdateDto>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Active WeeklySprintUpdates fetched successfully",
				paginated);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-sprint-id")
	public ResponseEntity<AuthResponse<ListContentWrapper<WeeklySprintUpdateDto>>> getUpdatesBySprintId(
			@RequestParam Long sprintId) {
		logger.info("Fetching WeeklySprintUpdates for sprintId: {}", sprintId);
		List<WeeklySprintUpdateDto> dtoList = service.getAllBySprintId(sprintId);
		String message = dtoList.isEmpty() ? "No weekly sprint updates available"
				: "Weekly sprint updates fetched successfully for sprintId: " + sprintId;
		AuthResponse<ListContentWrapper<WeeklySprintUpdateDto>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), message,
				new ListContentWrapper<>(dtoList.size(), dtoList));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/by-week-id")
	public ResponseEntity<AuthResponse<ListContentWrapper<WeeklySprintUpdateDto>>> getUpdatesByWeekId(
			@RequestParam int weekId) {
		logger.info("Fetching active WeeklySprintUpdates for weekId: {}", weekId);
		List<WeeklySprintUpdateDto> dtoList = service.getActiveUpdatesByWeekId(weekId);
		String message = dtoList.isEmpty() ? "No active WeeklySprintUpdates found for weekId: " + weekId
				: "Active WeeklySprintUpdates fetched successfully for weekId: " + weekId;
		AuthResponse<ListContentWrapper<WeeklySprintUpdateDto>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), message,
				new ListContentWrapper<>(dtoList.size(), dtoList));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/history-by-week-id")
	public ResponseEntity<AuthResponse<ListContentWrapper<WeeklySprintUpdateDto>>> getHistoricalUpdatesByWeekId(
			@RequestParam int weekId) {
		logger.info("Fetching historical WeeklySprintUpdates for weekId: {}", weekId);
		List<WeeklySprintUpdateDto> dtoList = service.getHistoricalUpdates(weekId);

		String message = dtoList.isEmpty() ? "No historical WeeklySprintUpdates available before weekId: " + weekId
				: "Historical WeeklySprintUpdates fetched successfully for weekId: " + weekId;

		AuthResponse<ListContentWrapper<WeeklySprintUpdateDto>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), message,
				new ListContentWrapper<>(dtoList.size(), dtoList));
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<WeeklySprintUpdateDto>> update(@PathVariable Integer id,
			@Valid @RequestBody WeeklySprintUpdateDto dto) {
		logger.info("Updating WeeklySprintUpdate ID: {}", id);
		WeeklySprintUpdateDto updatedDto = service.updateUpdate(id, dto);

		AuthResponse<WeeklySprintUpdateDto> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Weekly sprint update modified successfully",
				updatedDto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<Void>> softDelete(@PathVariable Integer id) {
		logger.info("Soft-deleting WeeklySprintUpdate ID: {}", id);
		service.deleteUpdate(id);

		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				"Weekly sprint update deactivated");
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/enable")
	public ResponseEntity<AuthResponse<Void>> enable(@PathVariable Integer id) {
		logger.info("Enabling WeeklySprintUpdate ID: {}", id);
		service.setWeeklySprintUpdateEnabled(id);

		AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				"Weekly sprint update enabled successfully");
		return ResponseEntity.ok(response);
	}
}
