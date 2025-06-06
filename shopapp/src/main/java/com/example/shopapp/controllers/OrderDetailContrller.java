package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.response.OrderDetailResponse;
import com.example.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailContrller {
    private final OrderDetailService orderDetailService;
    // add orderDetail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try {
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDeatil(orderDetailService.createOrderDetail(orderDetailDTO)) );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // lay ra 1 order detail voi id nao do
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        // return ResponseEntity.ok(orderDetailService.getOrderDetail(id)); // kiểu trả về khác
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDeatil(orderDetailService.getOrderDetail(id)));
    }

    // lay ra danh sach cac orderdetail cua 1 order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        List<OrderDetail> orderDetails= orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses=orderDetails.stream().map(OrderDetailResponse::fromOrderDeatil).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    // sua doi order detail
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOderDetail(@Valid @PathVariable("id") Long id,
                                              @RequestBody OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

        return ResponseEntity.ok( orderDetailService.updateOrderDetail(id,orderDetailDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body("delete successfully");
    }
    @GetMapping("/all")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(orderDetails);
    }
}
