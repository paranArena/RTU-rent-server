package com.RenToU.rentserver.dto.service;

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
public class ProductServiceDto {
    //other request var
    private long clubId;
    private long memberId;

    //mapped by CreateProductDto 
    private String name;
    private String category;
    private int price;
    private int quantity;
    private int maxRentalPeriod;
    private String pickupLocation;
    private String caution;

    //S3Service
    private String imagePath;

}
