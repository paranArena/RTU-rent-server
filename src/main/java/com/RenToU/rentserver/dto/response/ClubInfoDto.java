package com.RenToU.rentserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.RenToU.rentserver.domain.Club;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubInfoDto {

    private Long id;

    private String name;

    private String introduction;

    private String thumbnailPath;

    private List<String> hashtags;

    public static ClubInfoDto from(Club club) {
        if (club == null)
            return null;

        return ClubInfoDto.builder()
                .id(club.getId())
                .name(club.getName())
                .introduction(club.getIntroduction())
                .thumbnailPath(club.getThumbnailPath())
                .hashtags(club.getHashtagNames())
                .build();
    }
}
