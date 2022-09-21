package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.CouponMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponMemberRepository extends JpaRepository<CouponMember, Long> {

}
