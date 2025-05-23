package com.example.shopapp.repositories;

import com.example.shopapp.models.Category;
import com.example.shopapp.models.Order;
import com.example.shopapp.response.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    //  tìm đơn hàng 1 user nào đó
    List<Order> findByUserId(long userId);
}
