package com.RenToU.rentserver.dto.service;

import com.RenToU.rentserver.domain.RentalPolicy;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.List;

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
    @Mapping("name")
    private String name;
    @Mapping("category")
    private String category;
    @Mapping("quantity")
    private int quantity;
    @Mapping("location")
    private Point location;
    @Mapping("fifoRentalPeriod")
    private int fifoRentalPeriod;
    @Mapping("reserveRentalPeriod")
    private int reserveRentalPeriod;
    @Mapping("price")
    private int price;
    @Mapping("caution")
    private String caution;
    @Mapping("imagePath")
    private String imagePath;
    private List<RentalPolicy> rentalPolicies;


}
