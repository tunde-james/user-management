package com.example.usermanagement.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserReqDto(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = ".*[A-Z].*", message = "Password should contain at least 1 uppercase character")
        @Pattern(regexp = ".*[a-z].*", message = "Password should contain at least 1 lowercase character")
        @Pattern(regexp = ".*\\d.*", message = "Password should contain at least 1 number")
        String password) {}
