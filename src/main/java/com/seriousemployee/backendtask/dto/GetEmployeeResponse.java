package com.seriousemployee.backendtask.dto;

import com.seriousemployee.backendtask.entities.Employee;

public record GetEmployeeResponse(
        Long id,
        String name,
        String email,
        String role,
        String createdAt
) {
    public static GetEmployeeResponse fromEntity(Employee employee) {
        return new GetEmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getCreatedAt().toString()
        );
    }
}
