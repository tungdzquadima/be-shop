package com.example.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "orderId >= 1")
    private long orderId;

    @JsonProperty("product_id")
    @Min(value = 1,message = "orderId >= 1")
    private long productId;

    @Min(value = 0,message = "price >0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1,message = "numberOfProducts >= 1")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0,message = "totalMoney >0")
    private Float totalMoney;

    private String color;

}
