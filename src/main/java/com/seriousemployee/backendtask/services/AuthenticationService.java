package com.seriousemployee.backendtask.services;

import com.seriousemployee.backendtask.dto.LoginEmployeeRequest;
import com.seriousemployee.backendtask.dto.RegisterEmployeeRequest;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.repositories.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Employee signup(RegisterEmployeeRequest request) {
        Employee employee = new Employee(request.name(), request.email(), request.password(), "USER");
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    public Employee authenticate(LoginEmployeeRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));

        return employeeRepository.findByEmail(request.email()).orElseThrow();
    }
}
