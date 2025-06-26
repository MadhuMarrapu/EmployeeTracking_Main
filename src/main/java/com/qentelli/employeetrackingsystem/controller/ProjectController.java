package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
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
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse2;
import com.qentelli.employeetrackingsystem.serviceImpl.ProjectService;

@RestController
@RequestMapping("/Project/")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping()
    public ResponseEntity<?> createProject(@RequestBody ProjectDetailsDto dto) {
        Project project = projectService.createProject(dto);
        ProjectDetailsDto responseDto = modelMapper.map(project, ProjectDetailsDto.class);
        AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<>(
                HttpStatus.CREATED.value(),
                RequestProcessStatus.SUCCESS,
                "Project created successfully"
        );
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<AuthResponse<List<ProjectDetailsDto>>> getAllProjects() {
        List<ProjectDetailsDto> dtoList = projectService.getAllProjects();
        AuthResponse<List<ProjectDetailsDto>> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Projects fetched successfully",
                dtoList
        );
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthResponse<ProjectDetailsDto>> getProjectById(@PathVariable int id) {
        ProjectDetailsDto dto = projectService.getProjectById(id);
        AuthResponse<ProjectDetailsDto> authResponse = new AuthResponse<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                LocalDateTime.now(),
                "Project fetched successfully",
                dto
        );
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
        Project updated = projectService.updateProject(id, dto);
        ProjectDetailsDto responseDto = modelMapper.map(updated, ProjectDetailsDto.class);
        AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Project updated successfully"
        );
        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> partialUpdateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
        Project updated = projectService.partialUpdateProject(id, dto);
        ProjectDetailsDto responseDto = modelMapper.map(updated, ProjectDetailsDto.class);
        AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Project partially updated successfully"
        );
        return ResponseEntity.ok(authResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> softDeleteProject(@PathVariable int id) {
        projectService.softDeleteProject(id);
        AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Project soft deleted successfully"
        );
        return ResponseEntity.ok(authResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        AuthResponse2<ProjectDetailsDto> authResponse = new AuthResponse2<>(
                HttpStatus.OK.value(),
                RequestProcessStatus.SUCCESS,
                "Project deleted successfully"
        );
        return ResponseEntity.ok(authResponse);
    }
}
