package com.RenToU.rentserver.dto.response;

import java.time.LocalDateTime;

import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalStatus;

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
public class RentalInfoDto {

    private RentalStatus rentalStatus;

    private LocalDateTime rentDate;// 렌탈 시작 시간

    private LocalDateTime expDate;// 렌탈 만료 시간

    private boolean isMeRental;

    public static RentalInfoDto from(Rental rental) {
        if (rental == null)
            return null;
        return RentalInfoDto.builder()
                .rentalStatus(rental.getRentalStatus())
                .rentDate(rental.getRentDate())
                .expDate(rental.getExpDate())
                .build();
    }

    public static RentalInfoDto from(Rental rental, Long memberId) {
        if (rental == null)
            return null;
        return RentalInfoDto.builder()
                .rentalStatus(rental.getRentalStatus())
                .rentDate(rental.getRentDate())
                .expDate(rental.getExpDate())
                .isMeRental(rental.isMeRental(memberId))
                .build();
    }
}
