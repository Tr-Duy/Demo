package com.example.demo.dto.auth;

import java.time.Instant;

public record AuthResponse (
    String accessToken,
    String refreshToken,
    String tokenType,
    Instant expiresAt,
    Instant refeshExpiresAt,
    CurrentUserResponse user
    ){
}