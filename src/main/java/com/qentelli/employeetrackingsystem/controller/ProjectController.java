package com.qentelli.employeetrackingsystem.controller;

import java.util.List;

//import org.modelmapper.ModelMapper;
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

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.mapper.ModelMappers;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
//import com.qentelli.employeetrackingsystem.models.client.response.ProjectDto;
import com.qentelli.employeetrackingsystem.serviceImpl.ProjectService;

@RestController
@RequestMapping("/api")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	//@Autowired
	//ModelMapper modelMapper;
	
	//@Autowired
	//ModelMappers modelMappers;
	
	@PostMapping("/createProject")
	public ResponseEntity<?> createProject(@RequestBody ProjectDetailsDto projectDetailsDto){
		
	    Project project = projectService.createProject(projectDetailsDto);
		
		ProjectDetailsDto projectDetailsDto1 = ModelMappers.toDto(project);
		
		return new ResponseEntity<>("Project created successfully "+projectDetailsDto1.getProjectName(),HttpStatus.CREATED);
	}
	
	@GetMapping("/viewProjects")
	public ResponseEntity<List<ProjectDetailsDto>> getAllProjects(){
		
		List<ProjectDetailsDto> projectDetailsDto = projectService.getAllProjects();
		
		return new ResponseEntity<List<ProjectDetailsDto>>(projectDetailsDto,HttpStatus.OK);
		
	}
	
	@PutMapping("/updateProject/{id}")
	public ResponseEntity<?> updateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
	    Project updatedProject = projectService.updateProject(id, dto);
	    return ResponseEntity.ok("Project updated successfully: " + updatedProject.getProjectName());
	}
	
	@PatchMapping("/partialUpdateProject/{id}")
	public ResponseEntity<?> partialUpdateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
	    Project updatedProject = projectService.partialUpdateProject(id, dto);
	    return ResponseEntity.ok("Project partially updated: " + updatedProject.getProjectName());
	}

	@DeleteMapping("/deleteProject/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable int id) {
	    projectService.deleteProject(id);
	    return ResponseEntity.ok("Project deleted successfully with id: " + id);
	}
	
}
