package com.RenToU.rentserver.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.RenToU.rentserver.domain.CouponMemberHistory;

public interface CouponMemberHistoryRepository extends JpaRepository<CouponMemberHistory, Long> {

    @EntityGraph(attributePaths = { "coupon" })
    List<CouponMemberHistory> findAllByMemberId(long memberId);
}
