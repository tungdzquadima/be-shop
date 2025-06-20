package com.example.shopapp.repositories;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Boolean existsByName(String name);
    Page <Product> findAll(Pageable pageable);// phân trang sản phẩm


    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // tìm kiếm sản phẩm
    @Query("""
    SELECT p FROM Product p 
    JOIN p.brand b
    JOIN p.category c
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);




}
