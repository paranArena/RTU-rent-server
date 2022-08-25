package com.RenToU.rentserver.DTO.response;

import com.RenToU.rentserver.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String name;
    public static ProductDto from(Product product) {
        if(product == null) return null;

        return ProductDto.builder()
        .build();
    }
}
