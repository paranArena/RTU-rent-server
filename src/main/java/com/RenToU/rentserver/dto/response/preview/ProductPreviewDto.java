package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPreviewDto {
    private Long id;
    private String name;
    private int quantity;
    private int maxQuantity;
    private String clubName;
    private String thumbnailPath;
    private Long clubId;


    public static ProductPreviewDto from(Product product) {
        if (product == null)
            return null;
        return ProductPreviewDto.builder()
                .name(product.getName())
                .quantity(product.getItems().stream().filter(i -> i.getRental() == null).collect(Collectors.toList())
                        .size())
                .maxQuantity(product.getItems().size())
                .clubName(product.getClub().getName())
                .thumbnailPath(product.getImagePath())
                .clubId(product.getClub().getId())
                .id(product.getId())
                .build();
    }
}
