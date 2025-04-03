package com.demo.employeemanager.controllers;

import com.demo.employeemanager.models.dtos.EmployeeDTO;
import com.demo.employeemanager.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployees(@PathVariable(name = "employeeId") Long employeeId) {
        EmployeeDTO employee = employeeService.findEmployeeByID(employeeId);
        return employee != null? ResponseEntity.ok(employee): ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        EmployeeDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, @PathVariable(name = "employeeId") Long employeeId) {
        EmployeeDTO savedEmployee = employeeService.updateEmployee(employeeDTO, employeeId);
        return savedEmployee != null? ResponseEntity.ok(savedEmployee): ResponseEntity.notFound().build();
    }
}
