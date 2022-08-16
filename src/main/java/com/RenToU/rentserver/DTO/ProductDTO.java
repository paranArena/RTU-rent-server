package com.RenToU.rentserver.DTO;

import com.RenToU.rentserver.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
public class ProductDTO {
    //TODO 구현
    public static ProductDTO from(Product product){
        if(product == null) return null;

        return null;
    }
}
