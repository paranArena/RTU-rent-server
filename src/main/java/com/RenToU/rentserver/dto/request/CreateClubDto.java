package com.RenToU.rentserver.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.RenToU.rentserver.dto.response.ClubMemberDto;
import com.RenToU.rentserver.dto.response.NotificationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubDto {

    @NotBlank(message = "클럽 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "클럽 소개를 입력해주세요.")
    private String introduction;

    private String thumbnailPath;

    private List<String> hashtags;

    private List<ClubMemberDto> memberList;

    private List<NotificationDto> notifications;

    private List<CreateProductDto> products;

}
