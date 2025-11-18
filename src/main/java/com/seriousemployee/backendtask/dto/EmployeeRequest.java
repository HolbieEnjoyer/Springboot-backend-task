package com.seriousemployee.backendtask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
        @Pattern(regexp = "^[a-zA-Z]+(?: [a-zA-Z]+)*$", message = "Name can only contain alphabetic characters and spaces in between")
        String name,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format should be valid")
        @Size (max = 100, message = "Email must be less than 100 characters long")
        String email,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
        )
        String password,
        @NotBlank(message = "Role is mandatory")
        @Pattern(regexp = "^(ADMIN|USER)$", message = "Role must be ADMIN or USER")
        String role) { }
