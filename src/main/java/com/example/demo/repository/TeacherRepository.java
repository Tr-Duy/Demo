package com.example.demo.repository;

import com.example.demo.domain.entity.Teacher;
import com.example.demo.domain.enums.TeacherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    List<Teacher> findByStatus(TeacherStatus status);
    List<Teacher> findByIsActive(Boolean isActive);
    Optional<Teacher> findByTeacherCode(String teacherCode);
    boolean existsByTeacherCode(String teacherCode);
    boolean existsByPhone(String phone);
}
