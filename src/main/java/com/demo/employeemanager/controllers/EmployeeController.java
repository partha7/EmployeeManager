package com.demo.employeemanager.controllers;

import com.demo.employeemanager.exceptions.ResourceNotFoundException;
import com.demo.employeemanager.models.dtos.EmployeeDTO;
import com.demo.employeemanager.models.enums.Department;
import com.demo.employeemanager.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @GetMapping("/id/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(name = "employeeId") Long employeeId) {
        EmployeeDTO employee = employeeService.findEmployeeByID(employeeId);
        return employee != null? ResponseEntity.ok(employee): ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<EmployeeDTO> findEmployeeByName(@PathVariable(name = "name") String name) {
        return employeeService.findEmployeeByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with name: "+ name));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByFilter(@RequestParam(required = false) Integer minAge,
                                                                  @RequestParam(required = false) Integer maxAge,
                                                                  @RequestParam(required = false) Department department,
                                                                  @RequestParam(required = false) Double minSalary,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<EmployeeDTO> employees = employeeService.getEmployeesByFilter(minAge, maxAge, department, minSalary, sortBy, sortOrder, page, size);
        return ResponseEntity.ok(employees);
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

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable(name = "employeeId") Long employeeId) {
        Boolean deleted = employeeService.deleteEmployee(employeeId);
        return deleted ? ResponseEntity.ok(true) : new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}

