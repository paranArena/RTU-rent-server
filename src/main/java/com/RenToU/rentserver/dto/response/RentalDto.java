package com.RenToU.rentserver.dto.response;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;

import com.RenToU.rentserver.domain.ClubMember;
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
public class RentalDto {

    private Long id;

    private RentalStatus rentalStatus;

    private LocalDateTime rentDate;// 렌탈 시작 시간

    private LocalDateTime expDate;// 렌탈 만료 시간

    public static RentalDto from(Rental rental) {
        if (rental == null)
            return null;
        return RentalDto.builder()
        .id(rental.getId())
        .rentalStatus(rental.getRentalStatus())
        .rentDate(rental.getRentDate())
        .expDate(rental.getExpDate())
        .build();
    }
}
