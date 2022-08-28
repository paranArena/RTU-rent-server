package com.RenToU.rentserver.dto.response.preview;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.RenToU.rentserver.domain.Club;
import com.github.dozermapper.core.Mapping;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubPreviewDto {

    private Long id;

    private String name;

    private String introduction;

    private String thumbnailPath;

    private List<String> hashtags;

    public static ClubPreviewDto from(Club club){
        if(club == null) return null;

        return ClubPreviewDto.builder()
            .id(club.getId())
            .name(club.getName())
            .introduction(club.getIntroduction())
            .thumbnailPath(club.getThumbnailPath())
            .hashtags(club.getHashtagNames())
            .build();
    }
}


