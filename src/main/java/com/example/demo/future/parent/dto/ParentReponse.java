package com.example.demo.future.parent.dto;

import com.example.demo.future.parent.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentReponse {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private Gender gender;
    private String relationship;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
