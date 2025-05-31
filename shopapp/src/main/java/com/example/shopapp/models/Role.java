package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    public static String ADMIN="ADMIN";
    public static String USER="USER";

}
