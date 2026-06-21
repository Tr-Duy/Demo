package com.example.demo.future.Auth;


import com.example.demo.common.exception.BadRequestException;
import com.example.demo.common.exception.NotFoundException;
import com.example.demo.future.Auth.dto.AuthResponse;
import com.example.demo.future.Auth.dto.CurrentUserResponse;
import com.example.demo.future.Auth.dto.LoginRequest;
import com.example.demo.future.Auth.dto.RequestTokenRequest;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
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
    public CurrentUserResponse getCurrentUser(String username){
        if(username == null || username.isBlank()){ //throw dùng để chủ động ném ra một lỗi (Exception) nhằm lập tức dừng chương trình và báo cho hệ thống biết đang có sự cố xảy ra.
            throw  new BadRequestException("username khong duoc de trong");
        }
        User user = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new NotFoundException("User not found or inactive: " + username));
        return buildCurrentUser(user);
    }
    //ham tim user hoatdong dua vao username
    public User findActiveUserByUsername(String username){
        return userRepository.findByUsername(username)
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()))// dùng để lọc và chỉ giữ lại những người dùng (User) đang hoạt động.
                .orElseThrow(() -> new NotFoundException("User not found or inactive: "+ username));
    }//orElseThrow() dùng để lấy dữ liệu ra khỏi hộp Optional, nếu bên trong trống rỗng (null) thì lập tức ném ra một ngoại lệ (Exception).
    //xay dung token cho user
    public AuthResponse buildTokensForUser(User user,String rawPassword){
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
    @Transactional //dùng để gom nhiều hành động database vào một giao dịch duy nhất, đảm bảo nguyên tắc: Cùng thành công hoặc cùng thất bại
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(()-> new BadCredentialsException("Invalid credentials"));
        return  buildTokensForUser(user,request.password());
    }
    //ham xu ly refresh
    @Transactional
    public AuthResponse refresh(RequestTokenRequest request){

    }

}
