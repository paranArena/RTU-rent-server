package com.RenToU.rentserver.dto;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;

import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalStatus;
import com.RenToU.rentserver.dto.response.ClubMemberDto;

public class RentalDto {

    private Long id;

    private ClubMemberDto member;

    private RentalStatus rentalStatus;
    
    private LocalDateTime rentDate;//렌탈 시작 시간

    private LocalDateTime expDate;//렌탈 만료 시간


    public static RentalDto from(Rental rental){
        if(rental == null) return null;
        return null;
        // return RentalDto.builder()
        // .id(rental.getId())
        // .member(ClubMemberDto.from(rental.getMember()))
        // .rentalStatus(rental.getRentalStatus())
        // .rentDate(rental.getRentDate())
        // .expDate(rental.getExpDate())
        // .build();
    }
}
