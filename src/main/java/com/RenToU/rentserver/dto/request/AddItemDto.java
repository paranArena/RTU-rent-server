package com.RenToU.rentserver.dto.request;

import com.RenToU.rentserver.domain.RentalPolicy;

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
public class AddItemDto {
    private int numbering;
    private RentalPolicy rentalPolicy;

}
