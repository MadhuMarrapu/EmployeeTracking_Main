/*
package com.qentelli.employeetrackingsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.models.client.request.DailyUpdateDTO;
import com.qentelli.employeetrackingsystem.serviceImpl.DailyUpdateService;

@RestController
@RequestMapping("/api/daily-updates")
public class DailyUpdateController {

	private final DailyUpdateService dailyUpdateService;

	public DailyUpdateController(DailyUpdateService dailyUpdateService) {
		this.dailyUpdateService = dailyUpdateService;
	}

	@PostMapping("/create")
	public ResponseEntity<DailyUpdateDTO> create(@RequestBody DailyUpdateDTO dto) {
		return ResponseEntity.ok(dailyUpdateService.create(dto));
	}

	@GetMapping("/fetchAll")
	public ResponseEntity<List<DailyUpdateDTO>> getAll() {
		return ResponseEntity.ok(dailyUpdateService.getAll());
	}

	@GetMapping("/fetchById/{id}")
	public ResponseEntity<DailyUpdateDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(dailyUpdateService.getById(id));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<DailyUpdateDTO> update(@PathVariable Long id, @RequestBody DailyUpdateDTO dto) {
		return ResponseEntity.ok(dailyUpdateService.update(id, dto));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		dailyUpdateService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
*/
