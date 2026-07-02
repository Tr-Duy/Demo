package com.example.demo.future.teacher.dto;

import com.example.demo.future.teacher.domain.TeacherRole;
import com.example.demo.future.teacher.domain.TeacherStatus;
import lombok.Data;

@Data
public class TeacherReponse {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String relationship;
    private TeacherRole teacherRole;
    private TeacherStatus teacherStatus;
    private String cccdImageUrl;
    private Boolean isActive;
    private String achievement;
    private String workUnit;
    private String experience;
    private String description;
    private String salary;
    private Integer weeklySlots;
}
