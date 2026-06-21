package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class AuditableEntity extends BaseEntity{
    @CreationTimestamp //tu dong them tg
    @Column(name = "created_at", nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist //tu xu ly khi them moi
    private void save(){
        createdAt =LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    private void update(){
        updatedAt=LocalDateTime.now();
    }
}
