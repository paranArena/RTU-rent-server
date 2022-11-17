package com.RenToU.rentserver.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.RenToU.rentserver.domain.CouponMember;

public interface CouponMemberRepository extends JpaRepository<CouponMember, Long> {
    @EntityGraph(attributePaths = { "coupon" })
    List<CouponMember> findAllByMemberId(Long member_id);
}
