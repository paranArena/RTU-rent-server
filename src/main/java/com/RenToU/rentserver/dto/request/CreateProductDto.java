package com.RenToU.rentserver.dto.request;

import org.springframework.web.multipart.MultipartFile;

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
