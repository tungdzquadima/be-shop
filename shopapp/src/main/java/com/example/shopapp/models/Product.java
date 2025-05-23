package com.example.shopapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="products")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    @Column(name = "name",nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // mới thêm do chatgpt chỉ
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // hoặc dùng @JsonManagedReference nếu bạn dùng @JsonBackReference bên kia
    private List<ProductImage> images;


}
