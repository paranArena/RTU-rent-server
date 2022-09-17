package com.RenToU.rentserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductInfoDto {

    @NotBlank(message = "물품 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "물품 카테고리를 입력해주세요.")
    private String category;

    @NotNull(message = "물품 가격을 입력해주세요.")
    private int price;

    @NotNull(message = "렌탈 기간을 입력해주세요.")
    private int fifoRentalPeriod;

    @NotNull(message = "렌탈 기간을 입력해주세요.")
    private int reserveRentalPeriod;

    @NotBlank(message = "렌탈 장소 이름를 입력해주세요.")
    private String locationName;

    @NotNull(message = "장소의 좌표를 찾을 수 없습니다.")
    private Double latitude;

    @NotNull(message = "장소의 좌표를 찾을 수 없습니다.")
    private Double longitude;

    private String caution;

    private MultipartFile image;
}
