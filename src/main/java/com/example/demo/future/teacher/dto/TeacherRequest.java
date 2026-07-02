package com.example.demo.future.teacher.dto;

import com.example.demo.future.teacher.domain.TeacherRole;
import com.example.demo.future.teacher.domain.TeacherStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TeacherRequest {

    @NotBlank(message = "Teacher code cannot be blank")
    @Size(min = 3, max = 50, message = "Teacher code must be between 3 and 50 characters")
    private String teacher_code;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String full_name;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @Size(max = 100,message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200,message = "Address must be less than 200 characters")
    private String address;

    @NotBlank(message = "vai tro khong duoc de trong")
    private TeacherRole teacher_role;

    @NotBlank(message = "trang thai khong duoc de trong")
    private TeacherStatus teacher_status;

    private String cccdImageUrl;
    private Boolean is_active;
    private String achievement;
    private LocalDate dateOfBirth;
    private String description;
    private String experience;
    private BigDecimal salary;
    private Integer weeklySlots;
    private String workUnit;

}
