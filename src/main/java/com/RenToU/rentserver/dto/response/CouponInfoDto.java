package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfoDto {

    private Long id;

    private String name;

    private String information;

    private String imagePath;

    private LocationDto location;

    private LocalDateTime actDate;
    private LocalDateTime expDate;


    public static CouponInfoDto from(Coupon coupon) {
        if (coupon == null)
            return null;

        return CouponInfoDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .information(coupon.getInformation())
                .imagePath(coupon.getImagePath())
                .actDate(coupon.getActDate())
                .expDate(coupon.getExpDate())
                .location(LocationDto.from(coupon.getLocation()))
                .build();
    }
}
