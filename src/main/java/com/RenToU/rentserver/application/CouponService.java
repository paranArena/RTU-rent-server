package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.CouponMember;
import com.RenToU.rentserver.domain.CouponMemberHistory;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.service.CouponServiceDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CouponErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.CouponMemberHistoryRepository;
import com.RenToU.rentserver.infrastructure.CouponMemberRepository;
import com.RenToU.rentserver.infrastructure.CouponRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.RenToU.rentserver.domain.ClubRole.ADMIN;
import static com.RenToU.rentserver.domain.ClubRole.OWNER;
import static com.RenToU.rentserver.domain.ClubRole.USER;
import static com.RenToU.rentserver.domain.ClubRole.WAIT;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMemberRepository couponMemberRepository;
    private final CouponMemberHistoryRepository couponMemberHistoryRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Coupon createCouponAdmin(CouponServiceDto couponServiceDto) {
        Club club = findClub(couponServiceDto.getClubId());
        Long writerId = couponServiceDto.getMemberId();
        club.findClubMemberByMemberId(writerId).validateRole(true, OWNER, ADMIN);
        Coupon coupon = Coupon.createCoupon(couponServiceDto,club);
        couponRepository.save(coupon);
        clubRepository.save(club);
        return coupon;
    }

    public List<Coupon> getClubCouponsAdmin(long clubId, long memberId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        return club.getCoupons();
    }
    public List<CouponMember> getCouponMembersAdmin(long clubId, long memberId, long couponId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        return findCoupon(couponId).getMembers();
    }
    public List<CouponMemberHistory> getCouponMemberHistoriesAdmin(long clubId, long memberId, long couponId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        return findCoupon(couponId).getHistories();
    }
    @Transactional
    public void grantCouponAdmin(long granterId, long clubId, long couponId, List<Long> memberIds) {
        Member granter = findMember(granterId);
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(granterId).validateRole(true,OWNER,ADMIN);
        memberIds.stream().forEach(memberId->{
            club.findClubMemberByMemberId(memberId).validateRole(false,WAIT);
            Member member = findMember(memberId);
            member.getCoupons().stream().forEach(cm->cm.getId());
            Coupon coupon = findCoupon(couponId);
            CouponMember couponMember = CouponMember.createCouponMember(coupon,member,granter);
            couponMemberRepository.save(couponMember);
        });
    }
    @Transactional
    public void useCouponUser(long memberId, long clubId, long couponId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,USER,OWNER,ADMIN);
        Coupon coupon = findCoupon(couponId);
        coupon.vadlidateDate();
        CouponMember cm = coupon.findCouponMemberByMemberId(memberId);
        couponMemberHistoryRepository.save(CouponMemberHistory.createCouponMemberHistory(cm));
        cm.delete();
        couponMemberRepository.delete(cm);
    }
    public void updateCouponAdmin(Long couponId, CouponServiceDto dto) {
        Club club = findClub(dto.getClubId());
        club.findClubMemberByMemberId(dto.getMemberId()).validateRole(true,OWNER,ADMIN);
        Coupon coupon = findCoupon(couponId);
        coupon.update(dto);
        couponRepository.save(coupon);
    }
    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }
    private Coupon findCoupon(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CustomException(CouponErrorCode.COUPON_NOT_FOUND));
    }



}
