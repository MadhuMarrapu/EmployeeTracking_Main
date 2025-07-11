package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.exception.DuplicateProjectException;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	private final ProjectService projectService;

	@PostMapping
	public ResponseEntity<AuthResponse<ProjectRequest>> createProject(@Valid @RequestBody ProjectRequest projectRequest)
			throws DuplicateProjectException {
		logger.info("Creating new project with name: {}", projectRequest.getProjectName());
		ProjectRequest createdProject = projectService.create(projectRequest);

		logger.debug("Project created: {}", createdProject);
		AuthResponse<ProjectRequest> response = new AuthResponse<>(HttpStatus.CREATED.value(), RequestProcessStatus.SUCCESS,
				 "Project created successfully");

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<AuthResponse<List<ProjectRequest>>> getAllProjects() {
		logger.info("Fetching all projects");
		List<ProjectRequest> projectList = projectService.getAll();

		logger.debug("Number of projects fetched: {}", projectList.size());
		AuthResponse<List<ProjectRequest>> response = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Projects fetched successfully", projectList);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AuthResponse<ProjectRequest>> getProjectById(@PathVariable Integer id) {
		logger.info("Fetching project with ID: {}", id);
		ProjectRequest project = projectService.getById(id);

		logger.debug("Project fetched: {}", project);
		AuthResponse<ProjectRequest> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				LocalDateTime.now(), "Project fetched successfully", project);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthResponse<ProjectRequest>> updateProject(@PathVariable Integer id,
			@RequestBody ProjectRequest projectUpdate) {
		logger.info("Updating project with ID: {}", id);
		ProjectRequest updatedProject = projectService.update(id, projectUpdate);

		logger.debug("Project updated: {}", updatedProject);
		AuthResponse<ProjectRequest> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				 "Project updated successfully");

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/partialUpdateProject/{id}")
	public ResponseEntity<AuthResponse<ProjectRequest>> partiallyUpdateProject(@PathVariable int id,
			@RequestBody ProjectRequest patchDto) {
		logger.info("Partially updating project with ID: {}", id);
		ProjectRequest partiallyUpdatedProject = projectService.partialUpdateProject(id, patchDto);

		logger.debug("Project partially updated: {}", partiallyUpdatedProject);
		AuthResponse<ProjectRequest> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				 "Project partially updated successfully");

		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/softDeleteProject/{id}")
    public ResponseEntity<?> softDeleteProject(@PathVariable int id) {
        projectService.softDeleteProject(id);
        AuthResponse<ProjectRequest> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Project soft deleted successfully"
        );
        return ResponseEntity.ok(authResponse);
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<AuthResponse<ProjectRequest>> deleteProject(@PathVariable Integer id) {
		logger.info("Deleting project with ID: {}", id);
		projectService.deleteProject(id);

		logger.debug("Project with ID {} deleted", id);
		AuthResponse<ProjectRequest> response = new AuthResponse<>(HttpStatus.OK.value(), RequestProcessStatus.SUCCESS,
				"Project deleted successfully");

		return ResponseEntity.ok(response);
	}
}