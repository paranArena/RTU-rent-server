package com.RenToU.rentserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INACTIVE_USER(HttpStatus.FORBIDDEN, "비활성화 상태인 유저입니다."),
    // UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ""),
    NOT_AJOU_EMAIL(HttpStatus.BAD_REQUEST, "아주대학교 이메일이 아닙니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않거나 만료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
