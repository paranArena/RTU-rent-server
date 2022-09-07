package com.RenToU.rentserver.dto.response.preview;

import java.util.stream.Collectors;

import com.RenToU.rentserver.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPreviewDto {
    private Long id;

    private String name;

    private Long clubId;

    private String clubName;

    private String category;

    private String imagePath;

    private int left;

    private int max;

    public static ProductPreviewDto from(Product product) {
        if (product == null)
            return null;

        return ProductPreviewDto.builder()
                .id(product.getId())
                .name(product.getName())
                .clubId(product.getClub().getId())
                .clubName(product.getClub().getName())
                .category(product.getCategory())
                .imagePath(product.getImagePath())
                .left(product.getItems().stream()
                        .filter((item) -> item.getRental() == null)
                        .collect(Collectors.toList())
                        .size())
                .max(product.getItems().size())
                .build();
    }
}
