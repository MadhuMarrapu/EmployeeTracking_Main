package com.qentelli.employeetrackingsystem.controller;

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ReleaseRequestDTO;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ReleaseResponseDTO;
import com.qentelli.employeetrackingsystem.serviceImpl.ReleaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // ✅ Create
    @PostMapping("/save")
    public ResponseEntity<AuthResponse<String>> createRelease(@Valid @RequestBody ReleaseRequestDTO dto) {
        logger.info("Creating new release for project ID {}", dto.getProjectId());
        service.createRelease(dto);
        logger.info("Release created successfully");

        AuthResponse<String> response = new AuthResponse<>(HttpStatus.CREATED.value(),
                RequestProcessStatus.SUCCESS, "Release created successfully.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ Read All (Non-paginated)
	@GetMapping("/list")
	public ResponseEntity<AuthResponse<List<ReleaseResponseDTO>>> getAllReleases() {
		logger.info("Fetching all release entries");
		List<ReleaseResponseDTO> releaseList = service.getAllReleases();

		logger.info("Fetched {} release records", releaseList.size());

		AuthResponse<List<ReleaseResponseDTO>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Release data retrieved successfully", releaseList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

    // ✅ Read All (Paginated)
    @GetMapping("/paginated")
    public ResponseEntity<AuthResponse<PaginatedResponse<ReleaseResponseDTO>>> getPaginatedReleases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "releaseInformation") String sortBy
    ) {
        logger.info("Fetching paginated releases: page={}, size={}, sortBy={}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<ReleaseResponseDTO> releasePage = service.getPaginatedReleases(pageable);

        PaginatedResponse<ReleaseResponseDTO> paginated = new PaginatedResponse<>(
                releasePage.getContent(),
                releasePage.getNumber(),
                releasePage.getSize(),
                releasePage.getTotalElements(),
                releasePage.getTotalPages(),
                releasePage.isLast()
        );

        logger.debug("Paginated releases fetched: count={}, totalPages={}",
                releasePage.getNumberOfElements(), releasePage.getTotalPages());

        AuthResponse<PaginatedResponse<ReleaseResponseDTO>> response = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Paginated releases fetched successfully",
                paginated
        );

        return ResponseEntity.ok(response);
    }

    // ✅ Read by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthResponse<ReleaseResponseDTO>> getReleaseById(@PathVariable Long id) {
        logger.info("Fetching release with ID {}", id);
        ReleaseResponseDTO release = service.getReleaseById(id);

        AuthResponse<ReleaseResponseDTO> response = new AuthResponse<>(HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Release fetched successfully", release);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Update
    @PutMapping("/update/{id}")
    public ResponseEntity<AuthResponse<String>> updateRelease(@PathVariable Long id,
                                                              @Valid @RequestBody ReleaseRequestDTO dto) {
        logger.info("Updating release with ID {}", id);
        service.updateRelease(id, dto);
        logger.info("Release updated successfully");

        AuthResponse<String> response = new AuthResponse<>(HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS, "Release updated successfully.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ✅ Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AuthResponse<Void>> deleteRelease(@PathVariable Long id) {
        logger.info("Deleting release with ID {}", id);
        service.deleteRelease(id);
        logger.info("Release deleted successfully");

        AuthResponse<Void> response = new AuthResponse<>(HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Release deleted successfully", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
