package com.seriousemployee.backendtask.controllers;

import com.seriousemployee.backendtask.dto.*;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.entities.EmployeeSpecification;
import com.seriousemployee.backendtask.exception.SuperAdminException;
import com.seriousemployee.backendtask.security.EmployeeDetails;
import com.seriousemployee.backendtask.services.EmployeeService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/employees")
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

    @GetMapping("/view/{email}")
    public ResponseEntity<GetEmployeeResponse> getEmployeeById(@PathVariable String email,
                                                    @Parameter(hidden = true)
                                                    @RequestHeader(value = "Authorization", required = true) String authorization) {
        if (email.equals(superAdminDTO.getUsername()))
            throw new SuperAdminException("You are not authorized to access this information.");

        Employee employee = service.getEmployeeByEmail(email);
        GetEmployeeResponse response = GetEmployeeResponse.fromEntity(employee);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteMyAccount")
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

    @PutMapping("/updateMyInfo")
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
    @DeleteMapping("/delete/{id}")
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
    @PutMapping("/promote/{id}")
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
    @PutMapping("/demote/{id}")
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

    @GetMapping("/list")
    public ResponseEntity<GetEmployeePageResponse> listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String orderByDate,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateJoinedBefore,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateJoinedAfter,
            @Parameter(hidden = true)
            @RequestHeader(value = "Authorization", required = true) String authorization
            ) {
        Sort sort = orderByDate.equalsIgnoreCase("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);


        Specification<Employee> specification = EmployeeSpecification.build(role, dateJoinedBefore, dateJoinedAfter);

        Page<GetEmployeeResponse> response = service.findAllEmployees(specification, pageable).map(GetEmployeeResponse::fromEntity);

        GetEmployeePageResponse pageResponse = GetEmployeePageResponse.fromPage(response);
        return ResponseEntity.ok(pageResponse);
    }
}
