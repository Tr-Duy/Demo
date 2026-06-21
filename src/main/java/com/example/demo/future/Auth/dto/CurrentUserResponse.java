package com.example.demo.future.Auth.dto;

public record CurrentUserResponse(
        Long id,
        String username,
        String fullname,
        String role,
        Long parentId,
        Long teacherId
) {
}
