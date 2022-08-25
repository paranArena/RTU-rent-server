package com.RenToU.rentserver.DTO.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.RenToU.rentserver.DTO.response.ClubMemberDto;
import com.RenToU.rentserver.DTO.response.NotificationDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateClubDto {

    @NotBlank
    private String name;

    @NotBlank
    private String introduction;

    private String thumbnailPath;

    private List<String> hashtags;

    private List<ClubMemberDto> memberList;

    private List<NotificationDto> notifications;

    private List<CreateProductDto> products;

}
