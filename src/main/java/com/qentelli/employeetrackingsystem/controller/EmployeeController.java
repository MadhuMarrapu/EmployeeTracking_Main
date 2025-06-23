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

import com.qentelli.employeetrackingsystem.models.client.request.EmployeeDTO;
import com.qentelli.employeetrackingsystem.serviceImpl.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping("/create")
	public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO dto) {
		return ResponseEntity.ok(employeeService.create(dto));
	}

	@GetMapping("/fetchall")
	public ResponseEntity<List<EmployeeDTO>> getAll() {
		return ResponseEntity.ok(employeeService.getAll());
	}

	@GetMapping("/fetchById/{id}")
	public ResponseEntity<EmployeeDTO> getById(@PathVariable String id) {
		return ResponseEntity.ok(employeeService.getById(id));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<EmployeeDTO> update(@PathVariable String id, @RequestBody EmployeeDTO dto) {
		return ResponseEntity.ok(employeeService.update(id, dto));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		employeeService.delete(id);
		return ResponseEntity.noContent().build();
	}

}

*/