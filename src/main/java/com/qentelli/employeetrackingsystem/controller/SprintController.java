package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
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

import com.qentelli.employeetrackingsystem.entity.enums.SprintOrdinal;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.SprintRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.SprintResponse;
import com.qentelli.employeetrackingsystem.service.SprintService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sprints")
public class SprintController {

    private static final Logger logger = LoggerFactory.getLogger(SprintController.class);   
    private final SprintService sprintService;

    @PostMapping("/createSprint")
    public ResponseEntity<AuthResponse<Void>> create(@Valid @RequestBody SprintRequest request) {
        logger.info("Creating new sprint: {}", request);
        sprintService.createSprint(request);
        logger.info("Sprint created successfully");
        return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
                "Sprint created successfully", null));
    }

    @GetMapping("/getAllSprints")
    public ResponseEntity<AuthResponse<PaginatedResponse<SprintResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching all sprints - Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sprintId")
                .and(Sort.by(Sort.Direction.DESC, "fromDate")));

        Page<SprintResponse> responsePage = sprintService.getAllSprints(pageable);

        logger.info("Fetched {} sprints successfully", responsePage.getTotalElements());

        PaginatedResponse<SprintResponse> paginatedResponse = new PaginatedResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast());

        AuthResponse<PaginatedResponse<SprintResponse>> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Sprints fetched successfully",
                paginatedResponse);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<SprintResponse>> getById(@PathVariable Long id) {
        logger.info("Fetching sprint with ID {}", id);
        SprintResponse response = sprintService.getSprintById(id);
        logger.info("Sprint with ID {} retrieved successfully", id);
        return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
                "Sprint retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<SprintResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody SprintRequest request) {
        logger.info("Updating sprint with ID {}: {}", id, request);
        sprintService.updateSprint(id, request);
        logger.info("Sprint with ID {} updated successfully", id);
        return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
                "Sprint updated successfully", null));
    }

    /**
     * SOFT DELETE API
     */
    @DeleteMapping("/softDelete/{id}")
    public ResponseEntity<AuthResponse<Void>> softDelete(@PathVariable Long id) {
        logger.info("Soft deleting sprint with ID {}", id);
        sprintService.deleteSprint(id);
        logger.info("Sprint with ID {} soft deleted successfully", id);
        return ResponseEntity.ok(new AuthResponse<>(200, RequestProcessStatus.SUCCESS, LocalDateTime.now(),
                "Sprint soft deleted successfully", null));
    }
    
	@PutMapping("/toggle-enabled/{id}")
	public ResponseEntity<AuthResponse<Void>> toggleIsEnabled(@PathVariable Long id) {
		logger.info("Toggling isEnabled status for sprint with ID {}", id);
		boolean newStatus = sprintService.setSprintEnabled(id);
		logger.info("Sprint with ID {} isEnabled set to {}", id, newStatus);

		String message = newStatus ? "Sprint enabled successfully" : "Sprint disabled successfully";
		return ResponseEntity.ok(new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), message, null));
	}

	@GetMapping("/sprint-options")
	public List<String> getSprintOptions() {
		return Arrays.stream(SprintOrdinal.values()).map(s -> "Sprint-" + s.name().split("_")[1]).toList();
	}
}