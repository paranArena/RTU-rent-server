package com.RenToU.rentserver.exceptions.clubMember;

public class ClubMemberNotFoundException extends RuntimeException{
    public ClubMemberNotFoundException(Long id){
        super("유저가 클럽 가입 혹은 신청 대기 상태가 아닙니다.");
    }
}
