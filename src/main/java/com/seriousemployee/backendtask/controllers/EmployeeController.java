package com.seriousemployee.backendtask.controllers;

import com.seriousemployee.backendtask.dto.EmployeeDTO;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.services.EmployeeService;
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
    public Employee createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return service.createEmployee(employeeDTO.name(), employeeDTO.email(), employeeDTO.password(), employeeDTO.role());
    }

    @PutMapping("/updateEmployee/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        return service.updateEmployee(id, employeeDTO.name(), employeeDTO.email(), employeeDTO.password(), employeeDTO.role());
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
    }

    @GetMapping("/getEmployee/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return service.getEmployeeById(id);
    }

    @GetMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }
}
