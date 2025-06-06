package com.example.shopapp.models;

import jakarta.persistence.*;  // Đảm bảo bạn chỉ sử dụng jakarta.persistence
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tạo giá trị cho id
    private Long id;

    @Column(nullable = false)  // Đảm bảo cột name không được để trống
    private String name;
}
