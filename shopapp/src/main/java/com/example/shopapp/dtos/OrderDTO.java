package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @Min(value = 1,message = "userId phải lớn hơn 1")
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Tổng tiền phải lớn hơn 0")
    private Float totalMoney;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
private LocalDate shippingDate;


    public @Min(value = 1, message = "userId phải lớn hơn 1") Long getUserId() {
        return userId;
    }

    public void setUserId(@Min(value = 1, message = "userId phải lớn hơn 1") Long userId) {
        this.userId = userId;
    }



    public @NotBlank(message = "Số điện thoại không được bỏ trống") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Số điện thoại không được bỏ trống") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public @Min(value = 0, message = "Tổng tiền phải lớn hơn 0") Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(@Min(value = 0, message = "Tổng tiền phải lớn hơn 0") Float totalMoney) {
        this.totalMoney = totalMoney;
    }


}
