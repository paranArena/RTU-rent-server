package com.RenToU.rentserver.dto.response.preview;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubRole;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubPreviewDto {

    private Long id;

    private String name;

    private String introduction;

    private String thumbnailPath;

    private List<String> hashtags;

    private ClubRole clubRole;

    private int clubMemberSize;

    public static ClubPreviewDto from(Club club) {
        if (club == null)
            return null;

        return ClubPreviewDto.builder()
                .id(club.getId())
                .name(club.getName())
                .introduction(club.getIntroduction())
                .thumbnailPath(club.getThumbnailPath())
                .hashtags(club.getHashtagNames())
                .clubMemberSize(club.getMemberList().stream()
                        .filter((cm) -> cm.getRole() != ClubRole.WAIT)
                        .collect(Collectors.toList())
                        .size())
                .build();
    }
}
