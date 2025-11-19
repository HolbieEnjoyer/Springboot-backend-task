package com.seriousemployee.backendtask.dto;

public record GetEmployeeResponse(
        Long id,
        String name,
        String email,
        String role
) { }
