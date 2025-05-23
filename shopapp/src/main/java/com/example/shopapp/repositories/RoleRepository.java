package com.example.shopapp.repositories;

import com.example.shopapp.models.Category;
import com.example.shopapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
