package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

import org.springframework.web.server.ResponseStatusException;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.mapper.ModelMappers;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse2;
import com.qentelli.employeetrackingsystem.models.client.response.MessageResponse;

import com.qentelli.employeetrackingsystem.serviceImpl.ProjectService;

@RestController
@RequestMapping("/api")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@PostMapping("/createProject")
	public ResponseEntity<?> createProject(@RequestBody ProjectDetailsDto projectDetailsDto) {
		try {
			Project project = projectService.createProject(projectDetailsDto);
			ProjectDetailsDto projectDetailsDto1 = ModelMappers.toDto(project);
			AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<ProjectDetailsDto>(HttpStatus.OK.value(),
					RequestProcessStatus.SUCCESS, "Project created successfully");
			return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getMessage()));
		}
	}

	@GetMapping("/viewProjects")
	public ResponseEntity<AuthResponse<List<ProjectDetailsDto>>> getAllProjects() {

		List<ProjectDetailsDto> projectDetailsDto = projectService.getAllProjects();
		AuthResponse<List<ProjectDetailsDto>> authResponse = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Projects fetched successfully", projectDetailsDto);
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@PutMapping("/updateProject/{id}")
	public ResponseEntity<?> updateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
		Project updatedProject = projectService.updateProject(id, dto);
		ProjectDetailsDto projectDetailsDto1 = ModelMappers.toDto(updatedProject);
		AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<ProjectDetailsDto>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Project updated successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@PatchMapping("/partialUpdateProject/{id}")
	public ResponseEntity<?> partialUpdateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
		Project updatedProject = projectService.partialUpdateProject(id, dto);
		ProjectDetailsDto projectDetailsDto1 = ModelMappers.toDto(updatedProject);
		AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<ProjectDetailsDto>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Project partially updated successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@DeleteMapping("/softDeleteProject/{id}")
	public ResponseEntity<?> softDeleteProject(@PathVariable int id) {
		projectService.softDeleteProject(id);
		AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<ProjectDetailsDto>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Project soft deleted successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@DeleteMapping("/deleteProject/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable int id) {
		projectService.deleteProject(id);
		AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<ProjectDetailsDto>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Project deleted successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}
}
