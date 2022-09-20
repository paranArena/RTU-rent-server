package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.service.CouponServiceDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.CouponRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.RenToU.rentserver.domain.ClubRole.ADMIN;
import static com.RenToU.rentserver.domain.ClubRole.OWNER;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Coupon createCoupon(CouponServiceDto couponServiceDto) {
        Club club = findClub(couponServiceDto.getClubId());
        Long writerId = couponServiceDto.getMemberId();
        club.findClubMemberByMemberId(writerId).validateRole(true, OWNER, ADMIN);
        Coupon coupon = Coupon.createCoupon(couponServiceDto);
        couponRepository.save(coupon);
        return coupon;
    }

    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

}
