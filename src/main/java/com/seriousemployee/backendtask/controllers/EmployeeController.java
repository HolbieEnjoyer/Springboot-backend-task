package com.seriousemployee.backendtask.controllers;

import com.seriousemployee.backendtask.dto.GetEmployeeResponse;
import com.seriousemployee.backendtask.dto.RegisterEmployeeRequest;
import com.seriousemployee.backendtask.dto.RegisterEmployeeResponse;
import com.seriousemployee.backendtask.dto.SuperAdminDTO;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.exception.ResourceNotFoundException;
import com.seriousemployee.backendtask.exception.SuperAdminException;
import com.seriousemployee.backendtask.security.EmployeeDetails;
import com.seriousemployee.backendtask.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
public class EmployeeController {
    private final EmployeeService service;
    @Autowired
    private SuperAdminDTO superAdminDTO;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<Employee> getSelf(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = true) String authorization) {
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (employeeDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Employee employee = service.getEmployeeById(employeeDetails.getId());
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/employees/{email}")
    public ResponseEntity<GetEmployeeResponse> getEmployeeById(@PathVariable String email,
                                                    @Parameter(hidden = true)
                                                    @RequestHeader(value = "Authorization", required = true) String authorization) {
        if (email.equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("You are not authorized to access this information.");

        Employee employee = service.getEmployeeByEmail(email);
        GetEmployeeResponse response = new GetEmployeeResponse(employee.getId(), employee.getName(), employee.getEmail(), employee.getRole());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<Void> deleteAccount(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = true) String authorization) {
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (employeeDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (employeeDetails.getUsername().equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("Cannot delete Super Admin account");

        service.deleteEmployee(employeeDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody RegisterEmployeeRequest employeeRequest,
                                                   @Parameter(hidden = true)
                                                   @RequestHeader(value = "Authorization", required = true) String authorization) {
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (employeeDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (employeeDetails.getUsername().equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("Cannot update Super Admin account");

        Employee employee = service.updateEmployee(employeeDetails.getId(), employeeRequest, false);
        return ResponseEntity.ok(employee);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id,
                                               @Parameter(hidden = true)
                                               @RequestHeader(value = "Authorization", required = true) String authorization) {
        EmployeeDetails selfDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (selfDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Employee employee = service.getEmployeeById(id);

        if (employee.getEmail().equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("Cannot complete this action.");
        else if (employee.getRole().equals("ADMIN") && !selfDetails.getUsername().equals(superAdminDTO.getUsername()) && !selfDetails.getId().equals(id))
            throw new SuperAdminException("You do not have permission to delete this employee.");

        service.deleteEmployee(employee);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @PutMapping("/promoteEmployee/{id}")
    public ResponseEntity<RegisterEmployeeResponse> promoteEmployee(@PathVariable Long id,
                                                                    @Parameter(hidden = true)
                                                                    @RequestHeader(value = "Authorization", required = true) String authorization) {

        Employee employee = service.updateEmployee(id, null, true);
        if (employee.getEmail().equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("Cannot complete this action.");
        RegisterEmployeeResponse response = new RegisterEmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getCreatedAt().toString()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PutMapping("/demoteEmployee/{id}")
    public ResponseEntity<RegisterEmployeeResponse> demoteEmployee(@PathVariable Long id,
                                                                   @Parameter(hidden = true)
                                                                   @RequestHeader(value = "Authorization", required = true) String authorization){

        Employee employee = service.updateEmployee(id, null, false);
        if (employee.getEmail().equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("Cannot complete this action.");
        RegisterEmployeeResponse response = new RegisterEmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getCreatedAt().toString()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get All Employees", description = "Retrieve a list of all employees.")
    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees(
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = true) String authorization
    ) {
        List<Employee> employees = service.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
