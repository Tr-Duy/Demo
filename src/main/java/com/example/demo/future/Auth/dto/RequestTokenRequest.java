package com.example.demo.future.Auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestTokenRequest (
        @NotBlank
        String refeshToken
){
}
