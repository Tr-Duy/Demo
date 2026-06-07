package com.example.demo.dto.auth;

public record CurrentUserResponse(
    Long id,
    String username,
    String fullname,
    String role,
    Long parentId,
    Long teacherId){
}
