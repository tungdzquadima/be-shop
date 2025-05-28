package com.example.shopapp.response;

import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    @Id

    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty( "product_id ")
    private Long productId;

    @JsonProperty( "price")
    private Float price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty( "total_money")
    private Float totalMoney;

    private String color;

    public  static OrderDetailResponse fromOrderDeatil(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .color(orderDetail.getColor())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
