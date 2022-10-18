package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.dto.service.CouponServiceDto;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CouponErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder.Default
    @OneToMany(mappedBy = "coupon", fetch = LAZY, cascade = CascadeType.ALL)
    private List<CouponMember> members = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "coupon", fetch = LAZY, cascade = CascadeType.ALL)
    private List<CouponMemberHistory> histories = new ArrayList<>();

    public static Coupon createCoupon(CouponServiceDto dto, Club club) {
        Coupon coupon = Coupon.builder()
                .name(dto.getName())
                .imagePath(dto.getImagePath())
                .information(dto.getInformation())
                .actDate(dto.getActDate())
                .expDate(dto.getExpDate())
                .location(dto.getLocation())
                .build();
        club.addCoupon(coupon);
        return coupon;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public void addMember(CouponMember couponMember) {
        this.members.add(couponMember);
        couponMember.setCoupon(this);
    }

    public void addHistory(CouponMemberHistory couponMemberHistory) {
        this.histories.add(couponMemberHistory);
        couponMemberHistory.setCoupon(this);
    }

    public CouponMember findCouponMemberByMemberId(long memberId) {
        CouponMember CouponMember = this.getMembers().stream().filter(cm -> {
            return cm.getMember().getId().equals(memberId);
        }).findFirst().orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_MEMBER_NOT_FOUND));

        return CouponMember;
    }

    public void removeMember(CouponMember couponMember) {
        this.members.remove(couponMember);
    }

    public void vadlidateDate() {
        if (this.getActDate().isAfter(LocalDateTime.now())) {
            throw new CustomException(CouponErrorCode.COUPON_NOT_ACTIVATED);
        }
        if (this.getExpDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(CouponErrorCode.COUPON_EXPIRED);
        }
    }

    public void update(CouponServiceDto dto) {
        this.name = dto.getName();
        this.information = dto.getInformation();
        this.imagePath = dto.getImagePath();
        this.actDate = dto.getActDate();
        this.expDate = dto.getExpDate();
        this.location.setName(dto.getLocation().getName());
        this.location.setX(dto.getLocation().getX());
        this.location.setY(dto.getLocation().getY());
    }
}
