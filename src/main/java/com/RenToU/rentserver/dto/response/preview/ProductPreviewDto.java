package com.RenToU.rentserver.dto.response.preview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPreviewDto {

    private String name;

    // public static ProductPreviewDto from(Product product) {
    //     if(product == null) return null;

    //     return ProductPreviewDto.builder()
    //     .build();
    // }
}
