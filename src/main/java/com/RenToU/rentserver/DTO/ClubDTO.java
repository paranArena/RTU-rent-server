package com.RenToU.rentserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import com.RenToU.rentserver.domain.Club;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String introduction;

    private String thumbnailPath;

    public static ClubDTO from(Club club){
        if(club == null) return null;

        return ClubDTO.builder()
            .id(club.getId())
            .name(club.getName())
            .introduction(club.getIntroduction())
            .thumbnailPath(club.getThumbnailPath())
            .build();
    }
}

