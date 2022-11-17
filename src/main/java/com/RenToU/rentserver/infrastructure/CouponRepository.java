package com.RenToU.rentserver.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RenToU.rentserver.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
