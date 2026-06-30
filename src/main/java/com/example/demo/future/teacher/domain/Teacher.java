package com.example.demo.future.teacher.domain;

import com.example.demo.domain.AuditableEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@SQLDelete(sql = "UPDATE teachers SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Teacher extends AuditableEntity {

    private Boolean deleted = false;

    private String teacherCode;

    private String fullname;

    private String phone;

    private String email;

    private TeacherRole teacherRole;

    private TeacherStatus status = TeacherStatus.ACTIVE;

    private Boolean isActive = true;

    private LocalDateTime dateOfBirth;

    private BigDecimal salary;

    private Integer weeklySlots;

    private String address;

    private String workUnit;

    private String experience;

    private String achievement;

}
