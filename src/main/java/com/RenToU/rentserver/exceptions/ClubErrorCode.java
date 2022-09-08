package com.RenToU.rentserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubErrorCode implements ErrorCode {

    INACTIVE_USER(HttpStatus.FORBIDDEN, "비활성화 상태인 유저입니다."),
    // UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ""),
    NOT_AJOU_EMAIL(HttpStatus.BAD_REQUEST, "아주대학교 이메일이 아닙니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    DUP_CLUB_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 클럽 이름입니다."),
    CANT_JOIN_CLUB(HttpStatus.BAD_REQUEST, "멤버가 이미 가입하였거나, 가입 신청 상태입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
