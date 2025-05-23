package com.example.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "orderId >= 1")
    private int orderId;

    @JsonProperty("product_id")
    @Min(value = 1,message = "orderId >= 1")
    private int productId;

    @Min(value = 0,message = "price >0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1,message = "numberOfProducts >= 1")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0,message = "totalMoney >0")
    private int totalMoney;

    private String color;

    public OrderDetailDTO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
