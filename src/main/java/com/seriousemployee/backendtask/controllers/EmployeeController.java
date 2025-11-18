package com.seriousemployee.backendtask.controllers;

import com.seriousemployee.backendtask.dto.EmployeeRequest;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("/newEmployee")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = service.createEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = service.updateEmployee(id, employeeRequest);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = service.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = service.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
