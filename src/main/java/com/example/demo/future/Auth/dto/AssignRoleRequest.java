package com.example.demo.future.Auth.dto;

import com.example.demo.future.Auth.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotNull
    private UserRole role;
}
