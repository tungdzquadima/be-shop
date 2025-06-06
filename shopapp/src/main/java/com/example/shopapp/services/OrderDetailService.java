package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        // tìm xem orderID có tồn tại không
        Order order = orderRepository.findById((long)orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("cannot find order with id: "
                        +orderDetailDTO.getOrderId()));

        // tìm product id theo id

        Product product=productRepository.findById((long)orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("cannot find product with id : "
                        +orderDetailDTO.getProductId()));

        OrderDetail orderDetail= OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();

        // lưu vào cơ sở dữ liệu
        return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(()->new DataNotFoundException(
                "cannot find OrderDetail with id"+ id
        ));
    }

    @Override
    public OrderDetail updateOrderDetail(long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        // tìm xem orderDetail có tồn tại hay ko
        OrderDetail existingOrderDetail=orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("cannot find orderDetail with id: "+id));
        Order existingorder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->new DataNotFoundException("cannot find order with id: "+orderDetailDTO.getOrderId()));

        Product existingproduct=productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()->new DataNotFoundException("cannot find product with id : "
                        +orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setOrder(existingorder);
        existingOrderDetail.setProduct(existingproduct);

        return        orderDetailRepository.save(existingOrderDetail);

    }

    @Override
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
    // Lấy tất cả OrderDetail
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }
}
