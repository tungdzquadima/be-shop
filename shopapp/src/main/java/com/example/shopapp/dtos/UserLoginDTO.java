package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "password cannot be blank")
    private String password;

    public UserLoginDTO(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UserLoginDTO() {
    }

    public @NotBlank(message = "phone number is required") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "phone number is required") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "password cannot be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password cannot be blank") String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginDTO{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
