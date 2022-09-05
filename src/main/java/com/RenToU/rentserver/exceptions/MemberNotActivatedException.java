package com.RenToU.rentserver.exceptions;

public class MemberNotActivatedException extends RuntimeException {
    public MemberNotActivatedException(String email) {
        super("로그인을 위해서는 이메일 인증이 필요합니다.");
    }
}
