package com.RenToU.rentserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    INACTIVE_USER(HttpStatus.FORBIDDEN, "비활성화 상태인 유저입니다."),
    // UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ""),
    NOT_AJOU_EMAIL(HttpStatus.BAD_REQUEST, "아주대학교 이메일이 아닙니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    DUP_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUP_PHONE(HttpStatus.BAD_REQUEST, "이미 존재하는 휴대폰 번호입니다."),
    DUP_STUDENTID(HttpStatus.BAD_REQUEST, "이미 존재하는 학번입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
