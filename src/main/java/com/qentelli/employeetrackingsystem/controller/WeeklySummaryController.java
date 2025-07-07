package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.WeeklySummaryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/weekly-summary")
public class WeeklySummaryController {

    private static final Logger logger = LoggerFactory.getLogger(WeeklySummaryController.class);

    @Autowired
    private WeeklySummaryService weeklySummaryService;

    @PostMapping("/create-summary")
    public ResponseEntity<AuthResponse<WeeklySummaryResponse>> createWeeklySummary(
           @Valid @RequestBody WeeklySummaryRequest request) {

        logger.info("Creating weekly summary for week: {} - {}", request.getWeekStartDate(), request.getWeekEndDate());

        WeeklySummaryResponse response = weeklySummaryService.createSummary(request);
        logger.info("Weekly summary created with ID: {}", response.getWeekId());

        AuthResponse<WeeklySummaryResponse> authResponse = new AuthResponse<>(
                HttpStatus.CREATED.value(),
                RequestProcessStatus.SUCCESS,
                "Weekly summary created successfully"
        );
        authResponse.setData(response);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{weekId}")
    public ResponseEntity<AuthResponse<WeeklySummaryResponse>> getWeeklySummary(@PathVariable Integer weekId) {
        logger.info("Fetching weekly summary by ID: {}", weekId);

        WeeklySummaryResponse response = weeklySummaryService.getSummaryById(weekId);

        logger.info("Weekly summary retrieved: {}", response.getWeekRange());

        AuthResponse<WeeklySummaryResponse> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Weekly summary fetched successfully",
                response
        );
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<AuthResponse<List<WeeklySummaryResponse>>> getAllWeeklySummaries() {
        logger.info("Fetching all weekly summaries");
        List<WeeklySummaryResponse> responseList = weeklySummaryService.getAllSummaries();

        logger.info("Weekly summaries found: {}", responseList.size());

        AuthResponse<List<WeeklySummaryResponse>> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "All weekly summaries fetched successfully",
                responseList
        );
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PutMapping("/update-summary")
    public ResponseEntity<AuthResponse<WeeklySummaryResponse>> updateWeeklySummary(
          @Valid  @RequestBody WeeklySummaryRequest request) {

        logger.info("Updating weekly summary with ID: {}", request.getWeekId());

        WeeklySummaryResponse response = weeklySummaryService.updateSummary(request);

        logger.info("Weekly summary updated successfully for ID: {}", response.getWeekId());

        AuthResponse<WeeklySummaryResponse> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Weekly summary updated successfully"
        );
        authResponse.setData(response);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @DeleteMapping("/softDeleteSummary/{weekId}")
    public ResponseEntity<?> softDeleteAccount(@PathVariable int weekId) {
        logger.info("Soft deleting weekly summary with ID: {}", weekId);

        weeklySummaryService.softDeleteSummery(weekId);

        logger.info("Soft delete successful for ID: {}", weekId);

        AuthResponse<WeeklySummaryRequest> response = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "WeeklySummary soft deleted successfully"
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{weekId}")
    public ResponseEntity<AuthResponse<Void>> deleteWeeklySummary(@PathVariable Integer weekId) {
        logger.info("Hard deleting weekly summary with ID: {}", weekId);

        weeklySummaryService.deleteSummary(weekId);

        logger.info("Hard delete successful for ID: {}", weekId);

        AuthResponse<Void> authResponse = new AuthResponse<>(
                HttpStatus.NO_CONTENT.value(),
                RequestProcessStatus.SUCCESS,
                "Weekly summary deleted successfully"
        );
        return new ResponseEntity<>(authResponse, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/week-ranges")
    public ResponseEntity<AuthResponse<List<WeeklySummaryResponse>>> getWeekRanges() {
        logger.info("Fetching week ranges");

        List<WeeklySummaryResponse> responseList = weeklySummaryService.getFormattedWeekRanges();

        logger.info("Week ranges found: {}", responseList.size());

        AuthResponse<List<WeeklySummaryResponse>> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Weekly summary week ranges fetched successfully",
                responseList
        );

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
    
    @GetMapping("/by-range/{startDate}-To-{endDate}")
    public ResponseEntity<AuthResponse<WeeklySummaryResponse>> getByDateRange(
            @PathVariable String startDate,
            @PathVariable String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        WeeklySummaryResponse response = weeklySummaryService.getByStartAndEndDate(start, end);

        AuthResponse<WeeklySummaryResponse> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Weekly summary fetched successfully",
                response
        );

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
