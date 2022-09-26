package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.ClubErrorCode;
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
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponMember extends BaseTimeEntity {
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

    public static CouponMember createCouponMember(Coupon coupon, Member member, Member granter) {
        CouponMember couponMember = CouponMember.builder()
                .grantDate(LocalDateTime.now())
                .granter(granter)
                .build();

        coupon.addMember(couponMember);
        member.addCoupon(couponMember);
        return couponMember;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setMember(Member member) {
        this.member = member;
    }



    public String toString() {
        return this.getCoupon().getName() + " " + this.getMember().getName() + " " + this.getGranter();
    }

    public void delete() {
        member.removeCoupon(this);
        this.member = null;
        coupon.removeMember(this);
        this.coupon = null;

    }

//    public void delete() {
//        member.deleteCoupon(this);
//        coupon.deleteMember(this);
//    }
}
