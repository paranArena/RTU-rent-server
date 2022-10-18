package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfoDto {

    private Long id;

    private Long clubId;

    private String clubName;

    private String name;

    private Integer max;

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
                .clubId(coupon.getClub().getId())
                .clubName(coupon.getClub().getName())
                .name(coupon.getName())
                .max(coupon.getMax())
                .information(coupon.getInformation())
                .imagePath(coupon.getImagePath())
                .actDate(coupon.getActDate())
                .expDate(coupon.getExpDate())
                .location(LocationDto.from(coupon.getLocation()))
                .build();
    }
}
