package com.RenToU.rentserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import com.RenToU.rentserver.domain.Club;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubInfoDto {

    private Long id;

    private String name;

    private String introduction;

    private String thumbnailPath;

    private List<String> hashtags;

    // private List<ClubMemberDto> memberList;

    // private List<NotificationDto> notifications;

    // private List<ProductDto> products;
    
    public static ClubInfoDto from(Club club){
        if(club == null) return null;

        return ClubInfoDto.builder()
            .id(club.getId())
            .name(club.getName())
            .introduction(club.getIntroduction())
            .thumbnailPath(club.getThumbnailPath())
            .hashtags(club.getHashtagNames())
            // .memberList(club.getMemberList().stream()
            //     .map(member -> ClubMemberDto.from(member))
            //     .collect(Collectors.toList()))
            // .notifications(club.getNotifications().stream()
            //     .map(notification -> NotificationDto.from(notification))
            //     .collect(Collectors.toList()))
            // .products(club.getProducts().stream()
            //     .map(product -> ProductDto.from(product))
            //     .collect(Collectors.toList()))
            .build();
    }
}

