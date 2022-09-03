package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.RentalPolicy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private long id;

    private int numbering;

    private RentalPolicy rentalPolicy;

    private RentalDto rentalDto;

    public static ItemDto from(Item item){
        if(item == null) return null;

        return ItemDto.builder()
        .id(item.getId())
        .numbering(item.getNumbering())
        .rentalPolicy(item.getRentalPolicy())
        .rentalDto(RentalDto.from(item.getRental()))
        .build();
    }
}
