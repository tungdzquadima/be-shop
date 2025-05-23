package com.example.shopapp.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse extends BaseResponse{
    private long id;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("note")
    private String note;

    @JsonProperty("order_date")
    private LocalDate orderDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("total_money")
    private float totalMoney;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    //shipping_method
    @JsonProperty("shipping_method")
    private String shippingMethod;
    //shipping_date
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty( "tracking_number")
    private String trackingNumber;

    @JsonProperty( "payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private boolean active;
}
