package com.example.shopapp.repositories;

import com.example.shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
