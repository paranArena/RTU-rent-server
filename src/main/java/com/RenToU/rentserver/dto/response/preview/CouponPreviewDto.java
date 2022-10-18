package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.Notification;
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
public class CouponPreviewDto {

    private Long id;

    private Long clubId;

    private String clubName;

    private String name;

    private String imagePath;

    private LocalDateTime actDate;

    private LocalDateTime expDate;

    public static CouponPreviewDto from(Coupon coupon) {
        if (coupon == null)
            return null;

        return CouponPreviewDto.builder()
                .id(coupon.getId())
                .clubId(coupon.getClub().getId())
                .clubName(coupon.getClub().getName())
                .imagePath(coupon.getImagePath())
                .actDate(coupon.getActDate())
                .expDate(coupon.getExpDate())
                .name(coupon.getName())
                .build();
    }
}
