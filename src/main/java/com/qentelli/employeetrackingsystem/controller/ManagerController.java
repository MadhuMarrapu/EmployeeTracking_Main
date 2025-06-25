package com.qentelli.employeetrackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.models.client.request.ManagerDTO;
import com.qentelli.employeetrackingsystem.serviceImpl.ManagerService;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

	@Autowired
	private ManagerService managerService;

	@PostMapping
	public ManagerDTO createManager(@RequestBody ManagerDTO managerDTO) {
		return managerService.createManager(managerDTO);
	}

	@GetMapping
	public List<ManagerDTO> getAllManagers() {
		return managerService.getAllManagers();
	}

	@GetMapping("/{managerId}")
	public ManagerDTO getManagerById(@PathVariable String managerId) {
		return managerService.getManagerById(managerId);
	}

	@PutMapping("/{managerId}")
	public ManagerDTO updateManager(@PathVariable String managerId, @RequestBody ManagerDTO managerDTO) {
		return managerService.updateManager(managerId, managerDTO);
	}

	@DeleteMapping("/{managerId}")
	public void deleteManager(@PathVariable String managerId) {
		managerService.deleteManager(managerId);
	}
}