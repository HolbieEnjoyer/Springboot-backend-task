package com.seriousemployee.backendtask.services;

import com.seriousemployee.backendtask.entities.Employee;
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

    public Employee createEmployee(String name, String email, String password, String role) {
        return repo.save(new Employee(name, email, password, role));
    }

    public Employee updateEmployee(Long id, String name, String email, String password, String role) {
        Employee employee = repo.findById(id).orElse(null);
        if (employee != null) {
            employee.setName(name);
            employee.setEmail(email);
            employee.setPassword(password);
            employee.setRole(role);
            return repo.save(employee);
        }
        return null;
    }

    public Employee getEmployeeById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteEmployee(Long id) {
        repo.deleteById(id);
    }
}
