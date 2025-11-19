package com.seriousemployee.backendtask.dto;

public record LoginEmployeeResponse(
        Long id,
        String name,
        String email,
        String role,
        String createdAt,
        String token,
        String tokenExpiresIn
) { }
