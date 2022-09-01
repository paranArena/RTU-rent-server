package com.RenToU.rentserver.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.RenToU.rentserver.domain.RentalPolicy;
import com.github.dozermapper.core.Mapping;

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
public class CreateProductDto {

    private String name;

    private String category;

    private int price;

    private List<RentalPolicy> rentalPolicies;

    private int fifoRentalPeriod;

    private int reserveRentalPeriod;
    
    private String locationName;

    private Double latitude;

    private Double longitude;

    private String caution;
    
    private MultipartFile image;
}
