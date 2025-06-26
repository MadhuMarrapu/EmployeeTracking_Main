package com.qentelli.employeetrackingsystem.controller;

import com.qentelli.employeetrackingsystem.models.client.request.ManagerDTO;
import com.qentelli.employeetrackingsystem.serviceImpl.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping
    public ResponseEntity<ManagerDTO> create(@RequestBody ManagerDTO dto) {
        ManagerDTO created = managerService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAll() {
        return ResponseEntity.ok(managerService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(managerService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerDTO> update(@PathVariable Integer id, @RequestBody ManagerDTO dto) {
        return ResponseEntity.ok(managerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        managerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}