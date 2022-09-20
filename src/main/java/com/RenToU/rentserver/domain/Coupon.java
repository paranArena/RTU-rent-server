package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.dto.service.CouponServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    private String information;

    private String imagePath;

    private LocalDateTime actDate;

    private LocalDateTime expDate;

    private String caution;
    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "club_id")
    private Club club;


    public static Coupon createCoupon(CouponServiceDto dto) {
        Coupon coupon = Coupon.builder()
                .name(dto.getName())
                .imagePath(dto.getImagePath())
                .information(dto.getInformation())
                .actDate(dto.getActDate())
                .expDate(dto.getExpDate())
                .location(dto.getLocation())
                .build();
        return coupon;
    }
}
