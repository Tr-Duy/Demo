package com.example.demo.future.parent.domain;

import com.example.demo.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "parents")
@Data
@EqualsAndHashCode(callSuper = true)
public class Parent extends AuditableEntity {

    @Column(columnDefinition = "varchar(100)")
    private String fullname;

    @Column(columnDefinition = "varchar(20)")
    private String phone;

    @Column(columnDefinition = "varchar(200)")
    private String address;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private Gender gender = Gender.OTHER;

    @Column(length = 50)
    private String relationship;
}
