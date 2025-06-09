package com.example.shopapp.response;

import com.example.shopapp.models.BaseEntity;
import com.example.shopapp.models.Brand;
import com.example.shopapp.models.Category;
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
    private Category category;

    @JsonProperty("brand_id")
    private Brand brand;

    public static ProductResponse fromProduct(Product product) {
        System.out.println("Mapping product: " + product.getName());  // Log thêm tên sản phẩm

        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .category(product.getCategory())  // Kiểm tra xem Category có được ánh xạ đúng không
                .brand(product.getBrand())        // Kiểm tra xem Brand có được ánh xạ đúng không
                .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

}

