package com.RenToU.rentserver.dto.service;

import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.RentalPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class V1UpdateProductInfoServiceDto {
    // other request var
    private long clubId;
    private long memberId;
    private List<String> imagePaths;

    // mapped by CreateProductDto
    private String name;

    private String category;

    private int price;

    private List<RentalPolicy> rentalPolicies;

    private int fifoRentalPeriod;

    private int reserveRentalPeriod;

    private Location location;

    private String caution;
}
