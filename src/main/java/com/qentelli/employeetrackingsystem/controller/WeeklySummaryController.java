package com.qentelli.employeetrackingsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.serviceImpl.WeeklySummaryService;

@RestController
@RequestMapping("/weekly-summary")
public class WeeklySummaryController {

	private final WeeklySummaryService weeklySummaryService;

	public WeeklySummaryController(WeeklySummaryService weeklySummaryService) {
		this.weeklySummaryService = weeklySummaryService;
	}

	@PostMapping("/create/summary")
	public WeeklySummary createSummary(@RequestBody WeeklySummary summary) {
		return weeklySummaryService.createSummary(summary);
	}

	@GetMapping("/fetch/allsummaries")
	public List<WeeklySummary> getAllSummaries() {
		return weeklySummaryService.getAllSummaries();
	}

	@GetMapping("/fetch/summaryById/{id}")
	public Optional<WeeklySummary> getSummary(@PathVariable int id) {
		return weeklySummaryService.getSummaryById(id);
	}

	@PutMapping("/update/summaryById/{id}")
	public WeeklySummary updateSummary(@PathVariable int id, @RequestBody WeeklySummary summary) {
		return weeklySummaryService.updateSummary(id, summary);
	}

	@DeleteMapping("/delete/summaryById/{id}")
	public void deleteSummary(@PathVariable int id) {
		weeklySummaryService.deleteSummary(id);
	}
}