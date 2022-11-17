package com.RenToU.rentserver.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class V1UpdateProductInfoDto {

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

    private String locationName;

    private Double latitude;

    private Double longitude;

    private String caution;

    private List<String> imagePaths;
}
