package com.RenToU.rentserver.dto.request;

import com.RenToU.rentserver.domain.RentalPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductInfoDto {

    private String name;

    private String category;

    private int price;

    private int fifoRentalPeriod;

    private int reserveRentalPeriod;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private String caution;

    private MultipartFile image;
}