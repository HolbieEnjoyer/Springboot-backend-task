package com.seriousemployee.backendtask.controllers;

import com.seriousemployee.backendtask.dto.LoginEmployeeResponse;
import com.seriousemployee.backendtask.dto.RegisterEmployeeResponse;
import com.seriousemployee.backendtask.dto.LoginEmployeeRequest;
import com.seriousemployee.backendtask.dto.RegisterEmployeeRequest;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.security.EmployeeDetails;
import com.seriousemployee.backendtask.services.AuthenticationService;
import com.seriousemployee.backendtask.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterEmployeeResponse> register(@Valid @RequestBody RegisterEmployeeRequest request) {
        Employee registeredEmployee = authenticationService.signup(request);

        return ResponseEntity.ok(
                new RegisterEmployeeResponse(registeredEmployee.getId(),
                registeredEmployee.getName(),
                registeredEmployee.getEmail(),
                registeredEmployee.getRole(),
                registeredEmployee.getCreatedAt().toString()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginEmployeeResponse> authenticate(@Valid @RequestBody LoginEmployeeRequest request) {
        Employee authenticatedEmployee = authenticationService.authenticate(request);
        EmployeeDetails employeeDetails = new EmployeeDetails(authenticatedEmployee);

        String jwtToken = jwtService.generateToken(employeeDetails);

        return ResponseEntity.ok(
                new LoginEmployeeResponse(
                        authenticatedEmployee.getId(),
                        authenticatedEmployee.getName(),
                        authenticatedEmployee.getEmail(),
                        authenticatedEmployee.getRole(),
                        authenticatedEmployee.getCreatedAt().toString(),
                        jwtToken,
                        jwtService.getExpirationTime().toString()));
    }
}
