package com.example.shopapp.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@Data
public class BaseEntity {
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
    }
}
