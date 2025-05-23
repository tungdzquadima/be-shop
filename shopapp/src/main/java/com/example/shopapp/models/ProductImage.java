package com.example.shopapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_images")
@Builder
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore // hoặc dùng @JsonBackReference nếu dùng song song với @JsonManagedReference
    private Product product;



    @Column(name = "image_url",length = 300)
    private String imageUrl;

}
