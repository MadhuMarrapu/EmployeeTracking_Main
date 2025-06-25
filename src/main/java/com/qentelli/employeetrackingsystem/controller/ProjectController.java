package com.qentelli.employeetrackingsystem.controller;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.serviceImpl.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDetailsDto dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }    

    @GetMapping
    public ResponseEntity<List<ProjectDetailsDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsDto> getProjectById(@PathVariable int id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Project> partialUpdateProject(@PathVariable int id, @RequestBody ProjectDetailsDto dto) {
        return ResponseEntity.ok(projectService.partialUpdateProject(id, dto));
    }

    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Project> softDeleteProject(@PathVariable int id) {
        return ResponseEntity.ok(projectService.softDeleteProject(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable int id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }
}