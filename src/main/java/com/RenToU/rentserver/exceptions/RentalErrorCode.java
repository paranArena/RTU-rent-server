package com.RenToU.rentserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalErrorCode implements ErrorCode {

    NOT_CLUB_MEMBER(HttpStatus.BAD_REQUEST, "멤버가 속해있는 클럽의 아이템이 아닙니다."),

    RENTAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이템에 대한 렌탈을 찾을 수 없습니다."),
    WAIT_TIME_OVER(HttpStatus.BAD_REQUEST, "대기 시간이 초과되었습니다."),
    NOT_RENT_STATUS(HttpStatus.BAD_REQUEST, "아이템이 렌트 상태가 아닙니다."),
    NOT_WAIT_STATUS(HttpStatus.BAD_REQUEST, "아이템이 대기 상태가 아닙니다."),
    INVALID_MEMBER(HttpStatus.FORBIDDEN, "해당 아이템에 대한 권한이 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
