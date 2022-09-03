package com.RenToU.rentserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.dto.response.preview.ClubPreviewDto;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberClubDto {
    
    private Long id;

    private ClubPreviewDto club;

    private ClubRole clubRole;
    public static MemberClubDto from(ClubMember clubMember) {
        if(clubMember == null) return null;

        return MemberClubDto.builder()
            .id(clubMember.getId())
            .club(ClubPreviewDto.from(clubMember.getClub()))
            .clubRole(clubMember.getRole())
            .build();
    }
}

