package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailContrller {
    // add orderDetail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok("create order detail successfully "+orderDetailDTO.toString());
    }

    // lay ra 1 order detail voi id nao do
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.ok("get order detail with id = "+id);
    }

    // lay ra danh sach cac orderdetail cua 1 order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getDetails(@Valid @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok("getDetails with id = "+orderId);
    }

    // sua doi order detail
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOderDetail(@Valid @PathVariable("id") Long id,
                                              @RequestBody OrderDetailDTO neworderDetailDTO){
        return ResponseEntity.ok("update with id = "+id +", neworderDetailDTO = "+neworderDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.noContent().build();
    }
}
