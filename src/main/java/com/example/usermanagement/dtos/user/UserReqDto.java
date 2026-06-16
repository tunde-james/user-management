package com.example.usermanagement.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserReqDto(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        @Email(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Please provide a valid email address")
        String email) {}
