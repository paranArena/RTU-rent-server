package com.RenToU.rentserver.DTO.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDto {
    //TODO 구현
    private String name;
    private String category;
    private int price;
    private int quantity;
    private int maxRentalPeriod;
    private String pickupLocation;
    private String caution;
    private MultipartFile image;
}
