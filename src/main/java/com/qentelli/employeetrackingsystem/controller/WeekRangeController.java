package com.qentelli.employeetrackingsystem.controller;

import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.WeekRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/week-ranges")
public class WeekRangeController {

    @Autowired
    private WeekRangeService service;

    // POST call to save weekly data

    @PostMapping("/save")
    public ResponseEntity<String> saveWeeklyData(@RequestBody WeekRangeRequest request) {
        service.saveWeeklyData(request.getWeekFromDate(), request.getWeekToDate());
        return ResponseEntity.ok("Weekly data saved successfully.");

    }

    // GET call to generate report

    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generateReport(
            @RequestParam LocalDate weekFromDate,
            @RequestParam LocalDate weekToDate,
            Pageable pageable) {
        Page<WeekRangeResponse> report = service.generateReport(weekFromDate, weekToDate, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("content", report.getContent());
        response.put("pageable", Map.of(
                "pageNumber", report.getNumber(),
                "pageSize", report.getSize()
        ));

        return ResponseEntity.ok(response);

    }

}
 