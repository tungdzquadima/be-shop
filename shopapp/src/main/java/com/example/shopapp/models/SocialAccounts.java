package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "social_accounts")
@Builder
public class SocialAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id tự động tăng 1
    private Long id;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;//provider_id


    @Column(name = "provider_id", length = 50)
    private String providerId;

    @Column(name = "email", length = 50)
    private String email;


}

