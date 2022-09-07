package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.RentalPolicy;
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
public class AdminRentalPreviewDto {

    private String memberName;

    // rentalPreviewDto
    private Long id;

    private int numbering;

    private String name;

    private Long clubId;

    private String clubName;

    private String imagePath;

    private RentalPolicy rentalPolicy;

    private RentalInfoDto rentalInfo;

    public static AdminRentalPreviewDto from(Item item) {
        if (item == null)
            return null;

        return AdminRentalPreviewDto.builder()
                .memberName(item.getRental().getMember().getName())
                .id(item.getId())
                .numbering(item.getNumbering())
                .name(item.getProduct().getName())
                .clubId(item.getProduct().getClub().getId())
                .clubName(item.getProduct().getClub().getName())
                .imagePath(item.getProduct().getImagePath())
                .rentalPolicy(item.getRentalPolicy())
                .rentalInfo(RentalInfoDto.from(item.getRental()))
                .build();
    }
}
