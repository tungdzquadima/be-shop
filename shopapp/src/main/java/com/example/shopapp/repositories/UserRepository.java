package com.example.shopapp.repositories;

import com.example.shopapp.models.Category;
import com.example.shopapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
