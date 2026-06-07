package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RequestTokenRequest(
        @NotBlank String refreshToken
) {
}
