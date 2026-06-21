package com.example.demo.future.Auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank
        String currentPassword,
        @NotBlank
        @Size(min = 6, max =72 )
        String newPassword,
        @NotBlank
        String confirmPassword
) {
}
