package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySprintUpdateDto;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.WeeklySprintUpdateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weekly-sprint-update")
public class WeeklySprintUpdateController {

    private static final Logger logger = LoggerFactory.getLogger(WeeklySprintUpdateController.class);

    private final WeeklySprintUpdateService service;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AuthResponse<WeeklySprintUpdateDto>> create(@Valid @RequestBody WeeklySprintUpdateDto dto) {
        logger.info("Creating WeeklySprintUpdate for projectId={}, weekId={}", dto.getProjectId(), dto.getWeeekRangeId());

        WeeklySprintUpdate created = service.createUpdate(dto);
        WeeklySprintUpdateDto responseDto = modelMapper.map(created, WeeklySprintUpdateDto.class);

        logger.debug("WeeklySprintUpdate created: {}", responseDto);
        AuthResponse<WeeklySprintUpdateDto> response = new AuthResponse<>(
                HttpStatus.CREATED.value(),
                RequestProcessStatus.SUCCESS,
                "Weekly sprint update created successfully"
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<AuthResponse<PaginatedResponse<WeeklySprintUpdateDto>>> getAllActivePaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "weekSprintId") String sortBy) {

        logger.info("Fetching active WeeklySprintUpdates: page={}, size={}, sortBy={}", page, size, sortBy);
        sortBy = sortBy.trim(); // Remove whitespace or newline characters
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<WeeklySprintUpdate> updatePage = service.getAllUpdates(pageable);

        List<WeeklySprintUpdateDto> dtoList = updatePage.getContent().stream()
                .map(update -> modelMapper.map(update, WeeklySprintUpdateDto.class))
                .toList();

        PaginatedResponse<WeeklySprintUpdateDto> paginated = new PaginatedResponse<>(
                dtoList,
                updatePage.getNumber(),
                updatePage.getSize(),
                updatePage.getTotalElements(),
                updatePage.getTotalPages(),
                updatePage.isLast()
        );

        logger.debug("WeeklySprintUpdate page fetched: totalElements={}, totalPages={}",
                updatePage.getTotalElements(), updatePage.getTotalPages());

        AuthResponse<PaginatedResponse<WeeklySprintUpdateDto>> response = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Active WeeklySprintUpdates fetched successfully",
                paginated
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse<WeeklySprintUpdateDto>> update(
            @PathVariable Integer id,
            @RequestBody WeeklySprintUpdateDto dto) {

        logger.info("Updating WeeklySprintUpdate ID: {}", id);
        WeeklySprintUpdate updated = service.updateUpdate(id, dto);
        WeeklySprintUpdateDto responseDto = modelMapper.map(updated, WeeklySprintUpdateDto.class);

        logger.debug("WeeklySprintUpdate updated: {}", responseDto);
        AuthResponse<WeeklySprintUpdateDto> response = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Weekly sprint update modified successfully"
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AuthResponse<WeeklySprintUpdateDto>> softDelete(@PathVariable Integer id) {
        logger.info("Soft-deleting WeeklySprintUpdate ID: {}", id);
        service.deleteUpdate(id);

        AuthResponse<WeeklySprintUpdateDto> response = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Weekly sprint update deactivated"
        );
        return ResponseEntity.ok(response);
    }
}