package com.RenToU.rentserver.dto.response;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String GET_MEMBER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATE_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";

    public static final String REQUEST_CLUB_JOIN = "클럽 가입 신청 성공";
    public static final String GET_CLUB_JOIN = "클럽 가입 조회 성공";
    public static final String ACCEPT_CLUB_JOIN = "클럽 가입 승인 성공";

    public static final String CREATE_CLUB = "클럽 생성 성공";
    public static final String GET_CLUB = "클럽 조회 성공";
    public static final String UPDATE_CLUB = "클럽 수정 성공";
    public static final String DELETE_CLUB = "클럽 삭제 성공";

    public static final String CREATE_NOTIFICATION = "공지 생성 성공";
    public static final String GET_NOTIFICATION = "공지 생성 성공";
    public static final String UPDATE_NOTIFICATION = "공지 수정 성공";
    public static final String DELETE_NOTIFICATION = "공지 삭제 성공";
    public static final String SEARCH_NOTIFICATION_SUCCESS = "공지 검색 성공";
    public static final String SEARCH_NOTIFICATION_FIAL = "공지 검색 실패";


    public static final String SEARCH_CLUB_SUCCESS = "클럽 검색 성공";
    public static final String SEARCH_CLUB_FAIL = "클럽 검색 실패";
    
    public static final String CREATE_PRODUCT = "물품 생성 성공";

    public static final String RENT_REQUEST_SUCCESS = "렌탈 요청 성공";
    public static final String RENT_APPLY_SUCCESS = "렌탈 확정 성공";
    public static final String RENT_RETURN_SUCCESS = "렌탈 반납 성공";
    public static final String RENT_CANCEL_SUCCESS = "렌탈 취소 성공";
}
