package com.example.demo.future.parent.dto;

import com.example.demo.future.parent.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ParentRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Size(max = 100,message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @Size(max = 200,message = "Address must be less than 200 characters")
    private String address;

    private Gender gender=Gender.OTHER;

    @Size(max = 50,message = "Relationship must be less than 50 characters")
    private String relationship;
}
