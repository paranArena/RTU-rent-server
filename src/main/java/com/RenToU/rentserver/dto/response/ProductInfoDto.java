package com.RenToU.rentserver.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.RenToU.rentserver.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDto {
    private Long id;
    private String name;
    private String category;
    private LocationDto location;
    private int fifoRentalPeriod;

    private int reserveRentalPeriod;

    private int price;

    private String caution;

    private String imagePath;

    private List<ItemDto> items;

    public static ProductInfoDto from(Product product) {
        if (product == null)
            return null;

        return ProductInfoDto.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .location(LocationDto.from(product.getLocation()))
                .fifoRentalPeriod(product.getFifoRentalPeriod())
                .reserveRentalPeriod(product.getReserveRentalPeriod())
                .price(product.getPrice())
                .caution(product.getCaution())
                .imagePath(product.getImagePath())
                .items(product.getItems().stream().map((i) -> ItemDto.from(i)).collect(Collectors.toList()))
                .build();
    }

    public static ProductInfoDto from(Product product, Long memberId) {
        if (product == null)
            return null;

        return ProductInfoDto.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .location(LocationDto.from(product.getLocation()))
                .fifoRentalPeriod(product.getFifoRentalPeriod())
                .reserveRentalPeriod(product.getReserveRentalPeriod())
                .price(product.getPrice())
                .caution(product.getCaution())
                .imagePath(product.getImagePath())
                .items(product.getItems().stream().map((i) -> ItemDto.from(i, memberId)).collect(Collectors.toList()))
                .build();
    }
}
