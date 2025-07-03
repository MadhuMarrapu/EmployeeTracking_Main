package com.qentelli.employeetrackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.WeekRangeService;

@RestController
@RequestMapping("/api/week-ranges")
public class WeekRangeController {

    @Autowired
    private WeekRangeService service;

    @PostMapping("/generate")
    public ResponseEntity<List<WeekRangeResponse>> generateWeeks(@RequestBody WeekRangeRequest request) {
        List<WeekRangeResponse> ranges = service.generateWeeks(request.getStartDate());
        return ResponseEntity.ok(ranges);
    }

    @GetMapping
    public ResponseEntity<List<WeekRangeResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
