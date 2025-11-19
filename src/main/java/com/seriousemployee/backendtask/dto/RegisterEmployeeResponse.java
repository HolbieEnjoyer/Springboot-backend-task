package com.seriousemployee.backendtask.dto;

public record RegisterEmployeeResponse(
        Long id,
        String name,
        String email,
        String role,
        String createdAt
) { }
