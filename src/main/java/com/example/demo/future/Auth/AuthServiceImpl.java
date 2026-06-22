package com.example.demo.future.Auth;


import com.example.demo.common.exception.BadRequestException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.future.Auth.domain.UserRole;
import com.example.demo.future.Auth.dto.*;
import com.example.demo.future.Auth.domain.User;
import com.example.demo.future.RefreshToKenSession.RefreshToKenSessionRepository;
import com.example.demo.future.RefreshToKenSession.domain.RefreshTokenSession;
import com.example.demo.future.parent.ParentRepository;
import com.example.demo.future.teacher.TeacherRepository;
import com.example.demo.security.JwtService;
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
    private final RefreshToKenSessionRepository refreshToKenSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HttpServletRequest httpServletRequest;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;


    private CurrentUserResponse buildCurrentUser(User user) {
        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getFull_name(),
                user.getRole().name(),
                user.getParent() != null ? user.getParent().getId() : null,
                user.getTeacher() != null ? user.getTeacher().getId() : null
        );
    }

    //xuat thong tin user hien tai
    @Override
    public CurrentUserResponse getCurrentUser(String username){
        if(username == null || username.isBlank()){ //throw dùng để chủ động ném ra một lỗi (Exception) nhằm lập tức dừng chương trình và báo cho hệ thống biết đang có sự cố xảy ra.
            throw  new BadRequestException("username khong duoc de trong");
        }
        User user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new NotFoundException("User not found or inactive: " + username));
        return buildCurrentUser(user);
    }
    //ham tim user hoatdong dua vao username
    @Override
    public User findActiveUserByUsername(String username){
        return userRepository.findByUsername(username)
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()))// dùng để lọc và chỉ giữ lại những người dùng (User) đang hoạt động.
                .orElseThrow(() -> new NotFoundException("User not found or inactive: "+ username));
    }//orElseThrow() dùng để lấy dữ liệu ra khỏi hộp Optional, nếu bên trong trống rỗng (null) thì lập tức ném ra một ngoại lệ (Exception).
    //xay dung token cho user
    @Override
    public AuthResponse buildTokensForUser(User user, String rawPassword){
        if(!passwordEncoder.matches(rawPassword, user.getPasswordHash())){ //dùng để kiểm tra xem mật khẩu người dùng vừa nhập có trùng khớp với mật khẩu đã mã hóa lưu trong cơ sở dữ liệu hay không.
            throw new BadCredentialsException("Invalid credentials"); // tao 1 excep voi message
        }
        String accessToken = jwtService.generateAccessToken(user); // Tạo mới một Access Token (dùng để xác thực các yêu cầu gọi API)
        String refreshToken = jwtService.generateRefreshToken(user); // Tạo mới một Refresh Token (dùng để gia hạn khi Access Token hết hạn)
        String refreshJti = jwtService.extractJti(refreshToken);    // Bóc tách mã ID duy nhất (JTI) nằm bên trong Refresh Token vừa tạo
        Instant refreshExpiry = jwtService.extractExpiration(refreshToken); // Lấy ra thời gian hết hạn chính xác của Refresh Token đó


        RefreshTokenSession session =  new RefreshTokenSession();
        session.setJti(refreshJti);                       // Lưu ID duy nhất (JTI) của Refresh Token vào phiên làm việc
        session.setUser(user);                             // Gắn thông tin người dùng (User) sở hữu phiên làm việc này
        session.setExpiresAt(refreshExpiry);               // Thiết lập thời gian hết hạn của phiên làm việc (bằng hạn Token)
        session.setIsRevoked(false);                       // Đặt trạng thái ban đầu là "Chưa bị thu hồi" (vẫn đang hoạt động)
        session.setIpAddress(httpServletRequest.getRemoteAddr()); // Lấy và lưu địa chỉ IP mạng của thiết bị vừa gửi yêu cầu lên
        session.setUserAgent(httpServletRequest.getHeader("User-Agent")); // Lấy thông tin thiết bị/trình duyệt mà user đang sử dụng

        refreshToKenSessionRepository.save(session);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.extractExpiration(accessToken),
                refreshExpiry,
                buildCurrentUser(user));
    }
    //ham xu ly chuc nang login
    @Transactional
    @Override
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(()-> new BadCredentialsException("Invalid credentials"));
        return  buildTokensForUser(user,request.password());
    }
    //ham xu ly refresh
    @Transactional
    @Override
    public AuthResponse refresh(RequestTokenRequest request){
        String refreshToken = request.refeshToken();

        if(!jwtService.validateToken(refreshToken) || jwtService.isAccessToken(refreshToken)){
            throw new BadCredentialsException("Invalid refresh token");
        }
        String jti = jwtService.extractJti(refreshToken);
        RefreshTokenSession session = refreshToKenSessionRepository.findByJtiAndIsRevokedFalse(jti)
                .orElseThrow(()->new BadCredentialsException("Refresh token revoked or not found"));
        if(session.getExpiresAt().isBefore(Instant.now())){
            throw  new BadCredentialsException(("Refresh token expired"));
        }
        //thu hoi session cu
        session.setIsRevoked(true);
        session.setRevokedAt(Instant.now());
        //tao token moi
        User refreshUser = session.getUser();
        String newAccessToken = jwtService.generateAccessToken(refreshUser);
        String newRefreshToken = jwtService.generateRefreshToken(refreshUser);
        String newJti = jwtService.extractJti(newRefreshToken);
        Instant newRefreshExpiry = jwtService.extractExpiration(newRefreshToken);

        session.setReplacedByJti(newJti);
        refreshToKenSessionRepository.save(session);

        //luu session moi
        RefreshTokenSession newSession = new RefreshTokenSession();
        newSession.setJti(newJti);
        newSession.setUser(session.getUser());
        newSession.setExpiresAt(newRefreshExpiry);
        newSession.setIsRevoked(false);
        newSession.setIpAddress(httpServletRequest.getRemoteAddr());
        newSession.setUserAgent(httpServletRequest.getHeader("User-Agent"));

        //tra auth ve ng dung

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtService.extractExpiration(newAccessToken),
                newRefreshExpiry,
                buildCurrentUser(session.getUser()));
    }
    //ham tao user
    @Override
    public CurrentUserResponse createUser(CreateUserRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new com.example.demo.common.exception.ConfictException("Username already exists: " + request.getUsername());
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFull_name(request.getFullName());
        user.setRole(request.getRole());
        user.setIsActive(true);
        if(request.getParentId()!=null){
            user.setParent(parentRepository.findById(request.getParentId())
                    .orElseThrow(()-> new NotFoundException("Parent not found with id: " + request.getParentId())));
        }
        if(request.getTeacherId()!=null){
            user.setTeacher(teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(()-> new NotFoundException("Teacher not found with id: " + request.getTeacherId())));
        }
        return buildCurrentUser(userRepository.save(user));
    }
        //resert password for parent
        @Override
        public void resetpasswordForParent(Long parentUserId, String newPassword){
        User user = userRepository.findById(parentUserId)
                .orElseThrow(() -> new NotFoundException("User not found: " + parentUserId));
        if(user.getRole() != UserRole.PARENT){
            throw new BadRequestException("Only PARENT accounts can be reset via this endpoint");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    //danh sach user
    @Override
    public List<CurrentUserResponse> listUsers(){
        return userRepository.findAll().stream().map(this::buildCurrentUser).toList();
    }
    //lay user dua vao id
    @Override
    public CurrentUserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User not found: " + id));
        return buildCurrentUser(user);
    }
    //cấp quyền (gán vai trò mới) cho một người dùng dựa trên ID của họ.
    @Transactional
    @Override
    public CurrentUserResponse assignRole(Long id, AssignRoleRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: "+ id));
        user.setRole(request.getRole());
        return buildCurrentUser(userRepository.save(user));
    }

    //ham xu ly bat tat trang thai hoat dong user
    @Transactional
    @Override
    public CurrentUserResponse toggleActive(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User not found: " + id));
        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        return buildCurrentUser(userRepository.save(user));
    }
    //ham xu ly logout
    @Transactional
    @Override
    public void logout(String refreshToken){
        if(!jwtService.validateToken(refreshToken) || jwtService.isAccessToken(refreshToken)){
            throw new BadCredentialsException("Invalid rerfresh token");
        }
        String jti = jwtService.extractJti(refreshToken);
        // Tìm phiên làm việc trong database theo mã JTI và phải còn hạn/chưa bị hủy (IsRevokedFalse). Nếu tìm thấy (ifPresent) thì thực hiện tiếp
        refreshToKenSessionRepository.findByJtiAndIsRevokedFalse(jti).ifPresent(session -> {
            session.setIsRevoked(true);// Đổi trạng thái phiên làm việc thành true (Đã bị thu hồi / Bị khóa)
            session.setRevokedAt(Instant.now());   // Ghi nhận thời gian chính xác lúc phiên này bị hủy là ngay bây giờ            refreshToKenSessionRepository.save(session);
        });
    }
    //ham xu ly thay doi password
    @Override
    public void changePassword(String username, ChangePasswordRequest request){
        User user = findActiveUserByUsername(username);
        if(!passwordEncoder.matches(request.currentPassword(),user.getPasswordHash())){
            throw new BadCredentialsException("Curent password is incorrect");
        }
        if(!request.newPassword().equals(request.confirmPassword())){
            throw new BadCredentialsException("New password and confirm password do not match");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
