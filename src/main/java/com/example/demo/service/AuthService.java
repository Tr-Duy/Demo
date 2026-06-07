package com.example.demo.service;

import com.example.demo.common.exception.BadRequestException;
import com.example.demo.common.exception.NotFoundExeception;
import com.example.demo.domain.entity.Users;
import com.example.demo.dto.auth.*;

import java.util.List;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RequestTokenRequest request);
    AuthResponse buildTokensForUser(Users user, String rawPassword);
    CurrentUserResponse getCurrentUser(String username) throws BadRequestException, NotFoundExeception;
    Users findActiveUserByUsername(String username) throws NotFoundExeception;
    void changePassword(String username, ChangePasswordRequest request);
    CurrentUserResponse createUser(CreateUserRequest request);
    void resetPasswordForParent(Long parentUserId, String newPassword);
    List<CurrentUserResponse> listUsers();
    CurrentUserResponse getUserById(Long id);
    CurrentUserResponse assignRole(Long id, AssignRoleRequest request);
    CurrentUserResponse toggleActive(Long id);
    void logout(String refreshToken);
}
