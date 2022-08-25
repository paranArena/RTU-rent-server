package com.RenToU.rentserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubHashtag;

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

    private List<String> hashtags;

    private List<ClubMemberDTO> memberList;

    private List<NotificationDTO> notifications;

    private List<ProductDTO> products;
    
    public static ClubDTO from(Club club){
        if(club == null) return null;

        return ClubDTO.builder()
            .id(club.getId())
            .name(club.getName())
            .introduction(club.getIntroduction())
            .thumbnailPath(club.getThumbnailPath())
            .hashtags(club.getHashtagNames())
            .memberList(club.getMemberList().stream()
                .map(member -> ClubMemberDTO.from(member))
                .collect(Collectors.toList()))
            .notifications(club.getNotifications().stream()
                .map(notification -> NotificationDTO.from(notification))
                .collect(Collectors.toList()))
            .products(club.getProducts().stream()
                .map(product -> ProductDTO.from(product))
                .collect(Collectors.toList()))
            .build();
    }
}

