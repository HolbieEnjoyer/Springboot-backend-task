package com.seriousemployee.backendtask.services;

import com.seriousemployee.backendtask.dto.EmployeeRequest;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.exception.ResourceNotFoundException;
import com.seriousemployee.backendtask.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    public Employee createEmployee(EmployeeRequest employeeRequest) {
        return repo.save(new Employee(employeeRequest.name(), employeeRequest.email(), employeeRequest.password(), employeeRequest.role()));
    }

    public Employee updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employee = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        if (employee != null) {
            employee.setName(employeeRequest.name());
            employee.setEmail(employeeRequest.email());
            employee.setPassword(employeeRequest.password());
            employee.setRole(employeeRequest.role());
            return repo.save(employee);
        }
        return null;
    }

    public Employee getEmployeeById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public void deleteEmployee(Long id) {
        repo.deleteById(id);
    }
}
