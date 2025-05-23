package com.example.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name="categories")
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Column(name = "name",nullable = false)
    private String name;

}
