package com.example.demo.future.Auth.domain;

import com.example.demo.domain.AuditableEntity;
import com.example.demo.future.parent.domain.Parent;
import com.example.demo.future.teacher.domain.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends AuditableEntity {
    @Column(nullable = false,unique = true,length = 50)//khong duoc de trong , khong duo trung , lap, toi da 50 ki tu
    private String username;

    @Column(name ="password_hash", nullable = false, length = 255)
    private String passwordHash;
    @Column(name = "full_name", nullable = false,length = 100)
    private String full_name;

    @Column(length = 50)
    private String phone;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)//khi naogọi đến đối tượng đó (ví dụ: post.getCategory()), Spring Boot mới chạy lệnh SQL để lấy dữ liệu
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
