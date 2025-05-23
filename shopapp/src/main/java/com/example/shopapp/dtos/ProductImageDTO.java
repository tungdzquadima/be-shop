package com.example.shopapp.dtos;

import com.example.shopapp.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @Min(value = 1,message = "Product's ID must be > 0")
    @JoinColumn(name = "product_id")
    private long productId;

    @Size(min=5,max=200,message = "Image's name")
    @Column(name = "image_url")
    private String imageUrl;
}
