package com.main.hospitalrecordsystem.department.controller;

import com.main.hospitalrecordsystem.department.entity.Department;
import com.main.hospitalrecordsystem.department.service.DepartmentService;
import com.main.hospitalrecordsystem.dto.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospital/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<Department>> saveDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    @GetMapping("/paged")
    public ResponseEntity<ResponseStructure<List<Department>>> findAll(@RequestParam int page, @RequestParam int size) {
        return departmentService.findAll(page, size);
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<Department>>> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Department>> findById(@PathVariable Integer id) {
        return departmentService.findById(id);
    }

    @PutMapping
    public ResponseEntity<ResponseStructure<Department>> updateDepartment(@RequestBody Department department) {
        return departmentService.updateDepartment(department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<Department>> deleteDepartment(@PathVariable Integer id) {
        return departmentService.deleteDepartment(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseStructure<Department>> findByName(@PathVariable String name) {
        return departmentService.findByName(name);
    }
}