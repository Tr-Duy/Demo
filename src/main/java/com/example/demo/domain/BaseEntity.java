package com.example.demo.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
//chua primary key dung chung cho tat ca entity

@MappedSuperclass//gom cac cot vao mot lop cha khong can ke thua
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//database tu dong tang gia tri id
    private Long id;



}
