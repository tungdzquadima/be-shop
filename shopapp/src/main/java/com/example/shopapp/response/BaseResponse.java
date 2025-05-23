package com.example.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    @JsonProperty( "created_at")
    private LocalDateTime createdAt;

    @JsonProperty( "updated_at")
    private LocalDateTime updatedAt;
}
