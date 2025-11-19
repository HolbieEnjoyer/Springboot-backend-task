package com.seriousemployee.backendtask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginEmployeeRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format should be valid")
        @Size(max = 100, message = "Email must be less than 100 characters long")
        String email,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
        )
        String password
) { }
