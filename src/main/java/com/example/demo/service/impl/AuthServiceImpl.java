package com.example.demo.service.impl;

import com.example.demo.common.exception.BadRequestException;
import com.example.demo.common.exception.NotFoundExeception;
import com.example.demo.domain.entity.RefreshTokenSession;
import com.example.demo.domain.entity.Users;
import com.example.demo.dto.auth.*;
import com.example.demo.repository.ParentRepository;
import com.example.demo.repository.RefreshTokenSessionRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenSessionRepository refreshTokenSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HttpServletRequest httpRequest;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public CurrentUserResponse getCurrentUser(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username không được để trống");
        }
        Users user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new NotFoundExeception("User not found or inactive: " + username));
        return buildCurrentUser(user);
    }

    @Override
    public Users findActiveUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()))
                .orElseThrow(() -> new NotFoundExeception("User not found or inactive: " + username));
    }

    @Override
    public AuthResponse buildTokensForUser(Users user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        String refreshJti = jwtService.extractJti(refreshToken);
        Instant refreshExpiry = jwtService.extractExpiration(refreshToken);

        RefreshTokenSession session = new RefreshTokenSession();
        session.setJti(refreshJti);
        session.setUser(user);
        session.setExpiresAt(refreshExpiry);
        session.setIsRevoked(false);
        session.setIpAddress(httpRequest.getRemoteAddr());
        session.setUserAgent(httpRequest.getHeader("User-Agent"));
        refreshTokenSessionRepository.save(session);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.extractExpiration(accessToken),
                refreshExpiry,
                buildCurrentUser(user)
        );
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Users user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        return buildTokensForUser(user, request.password());
    }

    @Override
    @Transactional
    public AuthResponse refresh(RequestTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.validateToken(refreshToken) || jwtService.isAccessToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String jti = jwtService.extractJti(refreshToken);

        RefreshTokenSession session = refreshTokenSessionRepository.findByJtiAndIsRevokedFalse(jti)
                .orElseThrow(() -> new BadCredentialsException("Refresh token revoked or not found"));

        if (session.getExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token expired");
        }

        // thu hồi session cũ
        session.setIsRevoked(true);
        session.setRevokedAt(Instant.now());

        // tạo token mới
        Users refreshUser = session.getUser();
        String newAccessToken = jwtService.generateAccessToken(refreshUser);
        String newRefreshToken = jwtService.generateRefreshToken(refreshUser);
        String newJti = jwtService.extractJti(newRefreshToken);
        Instant newRefreshExpiry = jwtService.extractExpiration(newRefreshToken);

        session.setReplacedByJti(newJti);
        refreshTokenSessionRepository.save(session);

        // lưu session mới
        RefreshTokenSession newSession = new RefreshTokenSession();
        newSession.setJti(newJti);
        newSession.setUser(session.getUser());
        newSession.setExpiresAt(newRefreshExpiry);
        newSession.setIsRevoked(false);
        newSession.setIpAddress(httpRequest.getRemoteAddr());
        newSession.setUserAgent(httpRequest.getHeader("User-Agent"));
        refreshTokenSessionRepository.save(newSession);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtService.extractExpiration(newAccessToken),
                newRefreshExpiry,
                buildCurrentUser(session.getUser())
        );
    }

    @Override
    public CurrentUserResponse createUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new com.example.demo.common.exception.ConflictException("Username already exists: " + request.getUsername());
        }
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setIsActive(true);
        if (request.getParentId() != null) {
            user.setParent(parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundExeception("Parent not found: " + request.getParentId())));
        }
        if (request.getTeacherId() != null) {
            user.setTeacher(teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new NotFoundExeception("Teacher not found: " + request.getTeacherId())));
        }
        return buildCurrentUser(userRepository.save(user));
    }

    @Override
    public void resetPasswordForParent(Long parentUserId, String newPassword) {
        Users user = userRepository.findById(parentUserId)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + parentUserId));
        if (user.getRole() != com.example.demo.domain.enums.UserRole.PARENT) {
            throw new BadRequestException("Only PARENT accounts can be reset via this endpoint");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<CurrentUserResponse> listUsers() {
        return userRepository.findAll().stream().map(this::buildCurrentUser).toList();
    }

    @Override
    public CurrentUserResponse getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + id));
        return buildCurrentUser(user);
    }

    @Override
    @Transactional
    public CurrentUserResponse assignRole(Long id, AssignRoleRequest request) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + id));
        user.setRole(request.getRole());
        return buildCurrentUser(userRepository.save(user));
    }

    @Override
    @Transactional
    public CurrentUserResponse toggleActive(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeception("User not found: " + id));
        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        return buildCurrentUser(userRepository.save(user));
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        if (!jwtService.validateToken(refreshToken) || jwtService.isAccessToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        String jti = jwtService.extractJti(refreshToken);
        refreshTokenSessionRepository.findByJtiAndIsRevokedFalse(jti).ifPresent(session -> {
            session.setIsRevoked(true);
            session.setRevokedAt(Instant.now());
            refreshTokenSessionRepository.save(session);
        });
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        Users user = findActiveUserByUsername(username);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private CurrentUserResponse buildCurrentUser(Users user) {
        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole().name(),
                user.getParent() != null ? user.getParent().getId() : null,
                user.getTeacher() != null ? user.getTeacher().getId() : null
        );
    }
}
