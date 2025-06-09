package com.qentelli.employeetrackingsystem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.models.client.request.ProjectDto;
import com.qentelli.employeetrackingsystem.serviceImpl.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	private final ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping("/create/project")
	public ResponseEntity<String> createProject(@RequestBody ProjectDto dto) {
		projectService.createProject(dto);
		return ResponseEntity.ok("Project is created");
	}

	@GetMapping("/fetch/allProjects")
	public ResponseEntity<List<ProjectDto>> getAllProjects() {
		return ResponseEntity.ok(projectService.getAllProjects());
	}
}
