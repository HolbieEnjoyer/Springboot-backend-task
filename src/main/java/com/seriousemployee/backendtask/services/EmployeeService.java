package com.seriousemployee.backendtask.services;

import com.seriousemployee.backendtask.dto.RegisterEmployeeRequest;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.exception.ResourceNotFoundException;
import com.seriousemployee.backendtask.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    public Employee updateEmployee(Long id, RegisterEmployeeRequest employeeRequest, boolean promotion) {
        Employee employee = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        if (employee != null) {
            if (employeeRequest != null) {
                String encodedPassword = passwordEncoder.encode(employeeRequest.password());
                employee.setName(employeeRequest.name());
                employee.setEmail(employeeRequest.email());
                employee.setPassword(encodedPassword);
            }
            else {
                if (!employee.getRole().equals("SUPERADMIN")) {
                    employee.setRole(promotion ? "ADMIN" : "USER");
                }
            }

            return repo.save(employee);
        }
        return null;
    }

    public Employee getEmployeeById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee getEmployeeByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    public void deleteEmployee(Long id) {
        repo.deleteById(id);
    }

    public void deleteEmployee(Employee employee) {
        repo.delete(employee);
    }
}
