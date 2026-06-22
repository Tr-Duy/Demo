package com.example.demo.future.Auth.dto;

import com.example.demo.future.Auth.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank @Size(max = 50)
    String username;

    @NotBlank @Size(min =6,max =72)
    String password;

    @NotBlank @Size(max =100)
    String fullName;

    @NotNull
    UserRole role;
    Long parentId;
    Long TeacherId;
}
