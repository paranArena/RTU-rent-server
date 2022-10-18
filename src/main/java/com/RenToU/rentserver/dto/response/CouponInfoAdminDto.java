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
public class CouponInfoAdminDto {

    private Long id;

    private String name;

    private Integer max;

    private String information;

    private int allCouponCount;

    private int leftCouponCount;

    private String imagePath;

    private LocationDto location;

    private LocalDateTime actDate;
    private LocalDateTime expDate;

    public static CouponInfoAdminDto from(Coupon coupon) {
        if (coupon == null)
            return null;

        return CouponInfoAdminDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .max(coupon.getMax())
                .information(coupon.getInformation())
                .imagePath(coupon.getImagePath())
                .actDate(coupon.getActDate())
                .expDate(coupon.getExpDate())
                .location(LocationDto.from(coupon.getLocation()))
                .allCouponCount(coupon.getMembers().size() + coupon.getHistories().size())
                .leftCouponCount(coupon.getMembers().size())
                .build();
    }
}
