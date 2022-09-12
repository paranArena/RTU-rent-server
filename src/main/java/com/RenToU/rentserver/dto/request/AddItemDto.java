package com.RenToU.rentserver.dto.request;

import javax.validation.constraints.NotBlank;

import com.RenToU.rentserver.domain.RentalPolicy;

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
public class AddItemDto {

    @NotBlank(message = "아이템 번호를 입력해주세요.")
    private int numbering;

    @NotBlank(message = "렌트 정책을 설정해주세요.")
    private RentalPolicy rentalPolicy;
}
