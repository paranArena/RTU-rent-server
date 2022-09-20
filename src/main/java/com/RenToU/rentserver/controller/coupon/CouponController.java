package com.RenToU.rentserver.controller.coupon;

import com.RenToU.rentserver.application.CouponService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Coupon;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateCouponDto;
import com.RenToU.rentserver.dto.request.CreateNotificationDto;
import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.service.CouponServiceDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/coupon")
public class CouponController {

    private final MemberService memberService;

    private final CouponService couponService;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createCoupon(@PathVariable long clubId,
                                                @Valid @RequestBody CreateCouponDto createCouponDto) throws IOException {
        System.out.println("test");
        long memberId = memberService.getMyIdWithAuthorities();
        Location location = new Location(createCouponDto.getLocationName(), createCouponDto.getLatitude(),
                createCouponDto.getLongitude());
        CouponServiceDto couponServiceDto = CouponServiceDto.from(createCouponDto,clubId,memberId,location);
        couponService.createCoupon(couponServiceDto);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_COUPON));
    }


}
