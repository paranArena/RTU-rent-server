package com.RenToU.rentserver.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {

    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    COUPON_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"회원에게 쿠폰이 없습니다."),
    COUPON_NOT_ACTIVATED(HttpStatus.BAD_REQUEST, "아직 쿠폰 유효기간 전입니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST,"쿠폰의 유효기간이 만료되었습니다." );

    private final HttpStatus httpStatus;
    private final String message;
}
