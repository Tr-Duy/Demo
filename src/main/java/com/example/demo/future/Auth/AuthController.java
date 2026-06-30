package com.example.demo.future.Auth;

import com.example.demo.common.ApiResponse;
import com.example.demo.future.Auth.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    //ham xu ly login
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ApiResponse.success(authService.login(request), "Login successful");
    }
    //ham xu ly refresh
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RequestTokenRequest request) {
        return ApiResponse.success(authService.refresh(request), "Token refreshed");
    }
    //ham xu ly logout
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody RequestTokenRequest request) {
        authService.logout(request.refeshToken());
        return ApiResponse.successMessage("Logged out successfully");
    }
    //ham xu ly thay doi password
    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request){
        authService.changePassword(authentication.getName(),request);
        return ApiResponse.successMessage("Password changed successfully");
    }
    //ham lay thong tin
    @PostMapping("/me")
    public ApiResponse<CurrentUserResponse> me(Authentication authentication){
        return ApiResponse.success(authService.getCurrentUser(authentication.getName()));
    }
    //ham tao user
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CurrentUserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return ApiResponse.success(authService.createUser(request),"User created");
    }
    //ham lay list user
    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<CurrentUserResponse>> listUsers(){
        return ApiResponse.success(authService.listUsers());
    }
    //ham lay user dua vao id
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CurrentUserResponse> getUserById(@PathVariable Long id){
        return ApiResponse.success(authService.getUserById(id));
    }
    //ham cap quyen vai tro dua vao id
    @PatchMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CurrentUserResponse> assignRole(@PathVariable Long id, @Valid @RequestBody AssignRoleRequest request){
        return ApiResponse.success(authService.assignRole(id,request),"Role assigned");
    }
    /// ham bat tat hoat dong cua user
    @PatchMapping("/users/{id}/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CurrentUserResponse> toglleActive(@PathVariable Long id){
        return ApiResponse.success(authService.toggleActive(id), "User status updated");
    }
    //ham xu ly reset password for parent
    @PostMapping("/users/{userId}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> resetPasswordForParent(@PathVariable Long userId, @RequestBody ResetPasswordRequest request) {
        authService.resetpasswordForParent(userId, request.newPassword());
        return ApiResponse.successMessage("Password reset successfully");
    }

    record ResetPasswordRequest(String newPassword) {}


}
