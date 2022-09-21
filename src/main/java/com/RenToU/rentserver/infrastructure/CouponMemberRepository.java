package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.CouponMember;
import com.RenToU.rentserver.domain.Hashtag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponMemberRepository extends JpaRepository<CouponMember, Long> {
    @EntityGraph(attributePaths = {"coupon"})
    List<CouponMember> findByMemberId(Long member_id);
}
