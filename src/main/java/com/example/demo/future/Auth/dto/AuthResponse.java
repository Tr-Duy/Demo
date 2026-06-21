package com.example.demo.future.Auth.dto;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Instant expiresAt,
        Instant refeshExpiresAt,
        CurrentUserResponse user
) {

}
