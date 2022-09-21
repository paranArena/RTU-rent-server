package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.CouponMember;
import com.RenToU.rentserver.domain.CouponMemberHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponMemberHistoryRepository extends JpaRepository<CouponMemberHistory, Long> {

    @EntityGraph(attributePaths = {"coupon"})
    List<CouponMemberHistory> findAllByMemberId(long memberId);
}
