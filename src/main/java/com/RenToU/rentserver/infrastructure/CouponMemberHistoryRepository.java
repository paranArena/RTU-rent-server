package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.CouponMember;
import com.RenToU.rentserver.domain.CouponMemberHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponMemberHistoryRepository extends JpaRepository<CouponMemberHistory, Long> {

}
