package com.RenToU.rentserver.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode {

    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    COUPON_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"회원에게 쿠폰이 없습니다." );

    private final HttpStatus httpStatus;
    private final String message;
}
