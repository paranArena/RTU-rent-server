package com.RenToU.rentserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubErrorCode implements ErrorCode {

    NOT_AJOU_EMAIL(HttpStatus.BAD_REQUEST, "아주대학교 이메일이 아닙니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 올바르지 않습니다."),
    DUP_CLUB_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 클럽 이름입니다."),
    CANT_REQUEST_JOIN(HttpStatus.BAD_REQUEST, "대상이 이미 가입하였거나, 가입 신청 상태입니다."),
    CANT_GRANT_USER(HttpStatus.BAD_REQUEST, "유저로 권한 변경이 불가한 대상입니다."),
    CANT_GRANT_ADMIN(HttpStatus.BAD_REQUEST, "관리자로 권한 변경이 불가한 대상입니다."),
    NOT_WAIT_USER(HttpStatus.BAD_REQUEST, "대상이 대기 상태가 아닙니다."),

    NO_ADMIN_PERMISSION(HttpStatus.FORBIDDEN, "해당 클럽에 대한 관리자 권한이 없습니다."),
    NO_OWNER_PERMISSION(HttpStatus.FORBIDDEN, "해당 클럽에 대한 소유자 권한이 없습니다."),
    NO_USER_PERMISSION(HttpStatus.FORBIDDEN, "해당 클럽에 대한 접근 권한이 없습니다."),

    CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 클럽을 찾을 수 없습니다."),
    CLUBMEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 클럽에서 해당 멤버를 찾을 수 없습니다."),
    HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 클럽에 등록되지 않은 해쉬태그입니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 클럽에서 해당 공지사항을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 클럽에서 해당 물품을 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 클럽에서 해당 아이템을 찾을 수 없습니다."),
    NO_MATCHING_ROLE(HttpStatus.FORBIDDEN,"해당 작업을 수행하기 위한 역할이 없습니다." ),
    CLUB_OWNER_CANT_QUIT(HttpStatus.FORBIDDEN,"클럽장은 회원 탈퇴가 불가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
