package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.TechStackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/techStack")
public class TechStackController {

	private static final Logger logger = LoggerFactory.getLogger(TechStackController.class);

	private final TechStackService techStackService;

	@GetMapping
	public ResponseEntity<AuthResponse<List<String>>> getAllTechStacks() {
		logger.info("Fetching all tech stack entries (enum display names)");
		List<String> techStacks = techStackService.getAllTechStacks();

		logger.debug("Tech stacks fetched: {}", techStacks.size());
		AuthResponse<List<String>> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Tech stack list retrieved successfully", techStacks);

		return ResponseEntity.ok(response);
	}
}