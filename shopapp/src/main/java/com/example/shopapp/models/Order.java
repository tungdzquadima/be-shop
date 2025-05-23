package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Builder
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "total_money")
    private Float totalMoney;

    //shipping_address
    @Column(name = "shipping_address")
    private String shippingAddress;

    //shipping_method
    @Column(name = "shipping_method")
    private String shippingMethod;
    //shipping_date
    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number",nullable = false)
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "active")
    private boolean active;
}
