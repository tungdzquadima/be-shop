package com.example.shopapp.services;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderStatus;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.OrderRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // tìm xem userId có tồn tại ko
        User user=userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("cannot find user with ID = "+ orderDTO.getUserId()));
        // convert orderDTO sang order
        //dùng thư viện model mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));


        Order order=new Order();
        modelMapper.map(orderDTO,order);

        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTrackingNumber("TRK"+System.currentTimeMillis());
        // kiểm tra shipping date >= ngày hôm nay
        LocalDate shippingDate=orderDTO.getShippingDate()==null?LocalDate.now():orderDTO.getShippingDate();

        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override

    public Optional<Order> getOrder(long id) throws DataNotFoundException {
        return orderRepository.findById(id);
    }


    // phần này tự làm

    @Override
    public Order updateOrder(long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found with ID = " + id));

        // Cập nhật thông tin từ orderDTO vào order
        existingOrder.setAddress(orderDTO.getAddress());
        existingOrder.setEmail(orderDTO.getEmail());
        existingOrder.setFullname(orderDTO.getFullName());
        existingOrder.setNote(orderDTO.getNote());
        existingOrder.setPhoneNumber(orderDTO.getPhoneNumber());
        existingOrder.setShippingAddress(orderDTO.getShippingAddress());
        existingOrder.setShippingMethod(orderDTO.getShippingMethod());
        existingOrder.setShippingDate(orderDTO.getShippingDate());
        existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        existingOrder.setTotalMoney(orderDTO.getTotalMoney());
        existingOrder.setActive(true);

        // Trạng thái đơn có thể cập nhật nếu cần
        //existingOrder.setStatus(o);

        // Lưu lại
        return orderRepository.save(existingOrder);
    }


    @Override
    public void deleteOrder(long id) {

    }

    @Override
    public List<Order> getOrderFindByUser(long userId) {
        return orderRepository.findByUserId(userId);
    }
}
