package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id ")
    private Product product;

    @Column(name = "price",nullable = false)
    private Float price;

    //number_of_products
    @Column(name = "number_of_products",nullable = false)
    private int numberOfProducts;

    @Column(name = "total_money",nullable = false)
    private Float totalMoney;

    @Column(name = "color")
    private String color;
}
