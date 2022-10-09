package com.RenToU.rentserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalErrorCode implements ErrorCode {

    NOT_IN_SAME_CLUB(HttpStatus.BAD_REQUEST, "멤버가 속해있는 클럽의 아이템이 아닙니다."),
    WAIT_TIME_EXPIRED(HttpStatus.BAD_REQUEST, "대기 시간이 만료되었습니다."),
    NOT_RENT_STATUS(HttpStatus.BAD_REQUEST, "아이템이 렌트 상태가 아닙니다."),
    NOT_WAIT_STATUS(HttpStatus.BAD_REQUEST, "아이템이 대기 상태가 아닙니다."),
    DUP_ITEM_NUMBERING(HttpStatus.BAD_REQUEST, "이미 존재하는 아이템 번호입니다."),
    ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 예약 또는 대여 중인 아이템입니다."),

    NO_EDIT_PERMISSION(HttpStatus.FORBIDDEN, "해당 아이템에 대한 수정 권한이 없습니다."),

    RENTAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이템에 대한 렌탈을 찾을 수 없습니다."),
    SAME_STUDENTID_EXIST(HttpStatus.BAD_REQUEST,"해당 학번의 다른 이름을 가진 사용자가 존재합니다."),
    CANNOT_RENT(HttpStatus.BAD_REQUEST, "렌트할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
