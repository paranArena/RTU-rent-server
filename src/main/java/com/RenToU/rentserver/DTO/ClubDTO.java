package com.RenToU.rentserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.domain.Product;

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

    private List<ClubMember> memberList;

    private List<Notification> notifications;

    private List<Product> products;
    
    public static ClubDTO from(Club club){
        if(club == null) return null;

        return ClubDTO.builder()
            .id(club.getId())
            .name(club.getName())
            .introduction(club.getIntroduction())
            .thumbnailPath(club.getThumbnailPath())
            .memberList(club.getMemberList())
            .notifications(club.getNotifications())
            .products(club.getProducts())
            .build();
    }
}

