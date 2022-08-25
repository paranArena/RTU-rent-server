package com.RenToU.rentserver.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.RenToU.rentserver.domain.ClubMember;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberClubDto {
    
    private long id;

    private long clubId;

    private String name;

    private String introduction;
    
    private String thumbnailPath;

    public static MemberClubDto from(ClubMember clubMember) {
        if(clubMember == null) return null;

        return MemberClubDto.builder()
            .id(clubMember.getId())
            .clubId(clubMember.getClub().getId())
            .name(clubMember.getClub().getName())
            .introduction(clubMember.getClub().getIntroduction())
            .thumbnailPath(clubMember.getClub().getThumbnailPath())
            .build();
    }
}

