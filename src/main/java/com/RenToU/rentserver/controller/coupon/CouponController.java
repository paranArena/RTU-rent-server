package com.RenToU.rentserver.controller.coupon;

import com.RenToU.rentserver.application.CouponService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.CouponMember;
import com.RenToU.rentserver.domain.CouponMemberHistory;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateCouponDto;
import com.RenToU.rentserver.dto.request.CreateNotificationDto;
import com.RenToU.rentserver.dto.request.GrantCouponDto;
import com.RenToU.rentserver.dto.response.CouponInfoAdminDto;
import com.RenToU.rentserver.dto.response.CouponInfoDto;
import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.CouponMemberPreviewDto;
import com.RenToU.rentserver.dto.response.preview.CouponPreviewDto;
import com.RenToU.rentserver.dto.service.CouponServiceDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/")
public class CouponController {

    private final MemberService memberService;

    private final CouponService couponService;
    private final Mapper mapper;

    @PostMapping("coupons/admin")
    public ResponseEntity<?> createCouponAdmin(@PathVariable long clubId,
            @Valid @RequestBody CreateCouponDto createCouponDto) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        Location location = new Location(createCouponDto.getLocationName(), createCouponDto.getLatitude(),
                createCouponDto.getLongitude());
        CouponServiceDto couponServiceDto = CouponServiceDto.from(createCouponDto, clubId, memberId, location);
        couponService.createCouponAdmin(couponServiceDto);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_COUPON_ADMIN));
    }

    @GetMapping("coupons/admin")
    public ResponseEntity<?> getClubCouponsAdmin(@PathVariable long clubId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        List<Coupon> coupons = couponService.getClubCouponsAdmin(clubId, memberId);
        List<CouponPreviewDto> resData = coupons.stream().map(c -> CouponPreviewDto.from(c))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB_COUPONS_ADMIN, resData));
    }

    @GetMapping("coupons/{couponId}/couponMembers/admin")
    public ResponseEntity<?> getCouponMembersAdmin(@PathVariable long clubId, @PathVariable long couponId)
            throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        List<CouponMember> couponMembers = couponService.getCouponMembersAdmin(clubId, memberId, couponId);
        List<CouponMemberPreviewDto> resData = couponMembers.stream().map(cm -> CouponMemberPreviewDto.from(cm))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_COUPON_MEMBERS_ADMIN, resData));
    }

    @GetMapping("coupons/{couponId}/histories/admin")
    public ResponseEntity<?> getCouponMemberHistoriesAdmin(@PathVariable long clubId, @PathVariable long couponId)
            throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        List<CouponMemberHistory> histories = couponService.getCouponMemberHistoriesAdmin(clubId, memberId, couponId);
        List<CouponMemberPreviewDto> resData = histories.stream().map(cmh -> CouponMemberPreviewDto.from(cmh))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_COUPON_MEMBER_HISTORIES_ADMIN, resData));
    }

    @PostMapping("coupons/{couponId}/admin")
    public ResponseEntity<?> grantCouponAdmin(@PathVariable long clubId, @PathVariable long couponId,
            @Valid @RequestBody GrantCouponDto grantCouponDto) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        couponService.grantCouponAdmin(memberId, clubId, couponId, grantCouponDto.getMemberIds());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_COUPON_ADMIN));
    }

    @PutMapping("coupons/{couponId}/user")
    public ResponseEntity<?> useCouponUser(@PathVariable long clubId, @PathVariable long couponId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        couponService.useCouponUser(memberId, clubId, couponId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.USE_COUPON_USER));
    }

    @PutMapping("coupons/{couponId}/admin")
    public ResponseEntity<?> updateCouponAdmin(@PathVariable long clubId, @PathVariable long couponId,
            @Valid @RequestBody CreateCouponDto createCouponDto) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        Location location = new Location(createCouponDto.getLocationName(), createCouponDto.getLatitude(),
                createCouponDto.getLongitude());
        CouponServiceDto couponServiceDto = CouponServiceDto.from(createCouponDto, clubId, memberId, location);
        couponService.updateCouponAdmin(couponId, couponServiceDto);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_COUPON_ADMIN));
    }

    @GetMapping("coupons/{couponId}/user")
    public ResponseEntity<?> getCouponUser(@PathVariable long clubId, @PathVariable long couponId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        Coupon coupon = couponService.getCouponUser(clubId, memberId, couponId);
        CouponInfoDto resData = CouponInfoDto.from(coupon);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_COUPON_USER, resData));
    }

    @GetMapping("coupons/{couponId}/admin")
    public ResponseEntity<?> getCouponAdmin(@PathVariable long clubId, @PathVariable long couponId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        Coupon coupon = couponService.getCouponAdmin(clubId, memberId, couponId);
        CouponInfoAdminDto resData = CouponInfoAdminDto.from(coupon);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_COUPON_ADMIN, resData));
    }

    @DeleteMapping("couponMembers/{couponMemberId}/admin")
    public ResponseEntity<?> deleteCouponMemberAdmin(@PathVariable long clubId, @PathVariable long couponMemberId) {
        long memberId = memberService.getMyIdWithAuthorities();
        couponService.deleteCouponMemberAdmin(clubId, memberId, couponMemberId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_COUPON_MEMBER_ADMIN));
    }

    @DeleteMapping("coupons/{couponId}/admin")
    public ResponseEntity<?> deleteCouponAdmin(@PathVariable long clubId, @PathVariable long couponId) {
        long memberId = memberService.getMyIdWithAuthorities();
        couponService.deleteCouponAdmin(clubId, memberId, couponId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_COUPON_ADMIN));
    }

}
