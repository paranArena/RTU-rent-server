package com.RenToU.rentserver.dto.service;

import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.request.CreateItemDto;
import com.RenToU.rentserver.dto.request.LocationDto;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductServiceDto {
    //other request var
    private long clubId;
    private long memberId;
    private String imagePath;

    //mapped by CreateProductDto
    private String name;

    private String category;

    private int price;

    private List<RentalPolicy> rentalPolicies;

    private int fifoRentalPeriod;

    private int reserveRentalPeriod;

    private Location location;

    private String caution;
}
