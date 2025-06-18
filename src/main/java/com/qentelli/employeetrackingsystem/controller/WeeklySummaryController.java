/*
package com.qentelli.employeetrackingsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.models.client.request.CreateWeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.WeeklySummaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weekly-summary")
@RequiredArgsConstructor
public class WeeklySummaryController {

    private final WeeklySummaryService summaryService;

    @PostMapping("/create/weeklySummary")
    public ResponseEntity<WeeklySummaryResponse> createWeeklySummary(@RequestBody CreateWeeklySummaryRequest request) {
        return ResponseEntity.ok(summaryService.createWeeklySummary(request));
    }

    @GetMapping("/fetch/allSummaries")
    public ResponseEntity<List<WeeklySummaryResponse>> getAllSummaries() {
        return ResponseEntity.ok(summaryService.getAllSummaries());
    }
}
*/