package com.RenToU.rentserver.dto.service;

import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.dto.request.CreateCouponDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static org.springframework.data.convert.Jsr310Converters.StringToLocalDateTimeConverter.INSTANCE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponServiceDto {
    private long clubId;
    private long memberId;
    // mapped by CreateProductDto
    private String name;

    private Integer max;

    private LocalDateTime actDate;

    private LocalDateTime expDate;

    private String imagePath;

    private String information;

    private Location location;

    public static CouponServiceDto from(CreateCouponDto dto, Long clubId, Long memberId, Location location) {
        return CouponServiceDto.builder()
                .name(dto.getName())
                .max(dto.getMax())
                .information(dto.getInformation())
                .imagePath(dto.getImagePath())
                .expDate(INSTANCE.convert(dto.getExpDate()))
                .actDate(INSTANCE.convert(dto.getActDate()))
                .clubId(clubId)
                .memberId(memberId)
                .location(location)
                .build();
    }
}
