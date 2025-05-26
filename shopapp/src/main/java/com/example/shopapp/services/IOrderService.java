package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    Optional<Order> getOrder(long id) throws DataNotFoundException;
    Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(long id) throws DataNotFoundException;
    List<Order> getOrderFindByUser(long userId);
}
