package com.RenToU.rentserver.domain;

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
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponMemberHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "granter_id")
    private Member granter;

    private LocalDateTime grantDate;
    private LocalDateTime useDate;

    public static CouponMemberHistory createCouponMemberHistory(CouponMember couponMember) {
        CouponMemberHistory couponMemberHistory = CouponMemberHistory.builder()
                .member(couponMember.getMember())
                .granter(couponMember.getGranter())
                .grantDate(couponMember.getGrantDate())
                .useDate(LocalDateTime.now())
                .build();
        couponMember.getCoupon().addHistory(couponMemberHistory);
        return couponMemberHistory;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }


    public String toString() {
        return this.getCoupon().getName() + " " + this.getMember().getName() + " " + this.getGranter();
    }

//    public void delete() {
//        member.deleteCoupon(this);
//        coupon.deleteMember(this);
//    }
}
