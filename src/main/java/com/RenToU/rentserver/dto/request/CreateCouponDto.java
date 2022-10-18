package com.RenToU.rentserver.dto.request;

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
public class CreateCouponDto {

    private String name;

    private Integer max;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private String information;

    private String imagePath;

    private String actDate;

    private String expDate;

}
