package com.example.shopapp.response;

import com.example.shopapp.models.BaseEntity;
import com.example.shopapp.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private  String name;
    private float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private long categoryId;

    @JsonProperty("brand_id")
    private long brand;

    public  static ProductResponse fromProduct(Product product){
        ProductResponse productResponse=ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .brand(product.getBrand().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}

