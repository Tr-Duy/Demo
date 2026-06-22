package com.example.demo.future.Auth;

import com.example.demo.future.Auth.domain.User;
import com.example.demo.future.Auth.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthService {
    //xuat thong tin user hien tai
    CurrentUserResponse getCurrentUser(String username);
    //ham tim user hoatdong dua vao username
    User findActiveUserByUsername(String username)//orElseThrow() dùng để lấy dữ liệu ra khỏi hộp Optional, nếu bên trong trống rỗng (null) thì lập tức ném ra một ngoại lệ (Exception).
    ;
    //xay dung token cho user
    AuthResponse buildTokensForUser(User user, String rawPassword);
    //ham xu ly chuc nang login
    @Transactional
    //dùng để gom nhiều hành động database vào một giao dịch duy nhất, đảm bảo nguyên tắc: Cùng thành công hoặc cùng thất bại
    AuthResponse login(LoginRequest request);

    //ham xu ly refresh
    @Transactional
    AuthResponse refresh(RequestTokenRequest request);

    //ham tao user
    CurrentUserResponse createUser(CreateUserRequest request);

    //resert password for parent
    void resetpasswordForParent(Long parentUserId, String newPassword);

    //danh sach user
    List<CurrentUserResponse> listUsers();

    //lay user dua vao id
    CurrentUserResponse getUserById(Long id);

    //cấp quyền (gán vai trò mới) cho một người dùng dựa trên ID của họ.
    @Transactional
    CurrentUserResponse assignRole(Long id, AssignRoleRequest request);

    //ham xu ly bat tat trang thai hoat dong user
    @Transactional
    CurrentUserResponse toggleActive(Long id);

    //ham xu ly logout
    @Transactional
    void logout(String refreshToken);

    //ham xu ly thay doi password
    void changePassword(String username, ChangePasswordRequest request);
}
