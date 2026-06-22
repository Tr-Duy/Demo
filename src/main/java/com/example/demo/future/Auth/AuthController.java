package com.example.demo.future.Auth;

import com.example.demo.common.ApiResponse;
import com.example.demo.future.Auth.dto.AuthResponse;
import com.example.demo.future.Auth.dto.LoginRequest;
import com.example.demo.future.Auth.dto.RequestTokenRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
}
