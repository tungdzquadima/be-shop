package com.example.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import lombok.*;
//
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotEmpty (message ="tên không thể để trống")
    private String name;

}
