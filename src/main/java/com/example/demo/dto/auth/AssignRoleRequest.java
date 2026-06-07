package com.example.demo.dto.auth;

import com.example.demo.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotNull
    private UserRole role;
}