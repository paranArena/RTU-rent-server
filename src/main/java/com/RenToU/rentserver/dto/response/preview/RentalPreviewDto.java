package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.response.LocationDto;
import com.RenToU.rentserver.dto.response.RentalInfoDto;

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

    private Long productId;

    private String clubName;

    private String imagePath;

    private RentalPolicy rentalPolicy;

    private RentalInfoDto rentalInfo;

    private LocationDto location;

    public static RentalPreviewDto from(Item item) {
        if (item == null)
            return null;

        return RentalPreviewDto.builder()
                .id(item.getId())
                .numbering(item.getNumbering())
                .name(item.getProduct().getName())
                .clubId(item.getProduct().getClub().getId())
                .productId(item.getProduct().getId())
                .clubName(item.getProduct().getClub().getName())
                .imagePath(item.getProduct().getImagePath())
                .rentalPolicy(item.getRentalPolicy())
                .location(LocationDto.from(item.getProduct().getLocation()))
                .rentalInfo(RentalInfoDto.from(item.getRental()))
                .build();
    }

    public static RentalPreviewDto from(Item item, Long memberId) {
        if (item == null)
            return null;

        return RentalPreviewDto.builder()
                .id(item.getId())
                .numbering(item.getNumbering())
                .name(item.getProduct().getName())
                .clubId(item.getProduct().getClub().getId())
                .productId(item.getProduct().getId())
                .clubName(item.getProduct().getClub().getName())
                .imagePath(item.getProduct().getImagePath())
                .rentalPolicy(item.getRentalPolicy())
                .location(LocationDto.from(item.getProduct().getLocation()))
                .rentalInfo(RentalInfoDto.from(item.getRental(), memberId))
                .build();
    }
}
