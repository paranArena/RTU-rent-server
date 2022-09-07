package com.RenToU.rentserver.dto.response.preview;

import java.util.stream.Collectors;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.response.ItemDto;
import com.RenToU.rentserver.dto.response.RentalDto;

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
public class RentalPreviewDto {
    private Long id;

    private int numbering;

    private String name;

    private Long clubId;

    private String clubName;

    private String imagePath;

    private RentalPolicy rentalPolicy;

    private RentalDto rentalInfo;

    public static RentalPreviewDto from(Item item) {
        if (item == null)
            return null;

        return RentalPreviewDto.builder()
                .id(item.getId())
                .numbering(item.getNumbering())
                .name(item.getProduct().getName())
                .clubId(item.getProduct().getClub().getId())
                .clubName(item.getProduct().getClub().getName())
                .imagePath(item.getProduct().getImagePath())
                .rentalPolicy(item.getRentalPolicy())
                .rentalInfo(RentalDto.from(item.getRental()))
                .build();
    }
}
