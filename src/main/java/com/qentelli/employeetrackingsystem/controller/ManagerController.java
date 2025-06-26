<<<<<<< HEAD
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
=======
//package com.qentelli.employeetrackingsystem.controller;
//
//import com.qentelli.employeetrackingsystem.models.client.request.ManagerDTO;
//import com.qentelli.employeetrackingsystem.serviceImpl.ManagerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/managers")
//public class ManagerController {
//
//    @Autowired
//    private ManagerService managerService;
//
//    @GetMapping
//    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
//        return ResponseEntity.ok(managerService.getAllManagers());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Integer id) {
//        ManagerDTO manager = managerService.getManagerById(id);
//        return (manager != null)
//                ? ResponseEntity.ok(manager)
//                : ResponseEntity.notFound().build();
//    }
//
//    @PostMapping
//    public ResponseEntity<ManagerDTO> createManager(@RequestBody ManagerDTO dto) {
//        return ResponseEntity.ok(managerService.createManager(dto));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ManagerDTO> updateManager(@PathVariable Integer id, @RequestBody ManagerDTO dto) {
//        return ResponseEntity.ok(managerService.updateManager(id, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteManager(@PathVariable Integer id) {
//        managerService.deleteManager(id);
//        return ResponseEntity.noContent().build();
//    }
//}
>>>>>>> 7c12d92743737efb9d27729fe47a2cd9ef1f8869
