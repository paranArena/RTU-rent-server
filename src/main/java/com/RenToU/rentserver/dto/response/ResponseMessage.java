package com.RenToU.rentserver.dto.response;

public class ResponseMessage {
    public static final String OK = "요청 성공";

    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String GET_MEMBER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATE_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";

    public static final String GET_MY_CLUB_ROLE = "나의 클럽 역할 조회 성공";
    public static final String GET_CLUB_MEMBER = "클럽 멤버 조회 성공";

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
    public static final String GET_NOTIFICATION = "공지 조회 성공";
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
    public static final String GET_RENT = "렌탈 조회 성공";

    public static final String GET_MY_CLUB_REQUESTS = "나의 클럽 요청 조회 성공";

    public static final String GET_MY_RENT = "나의 렌트 상황 조회 성공";

    public static final String GET_MY_NOTIFICATION = "나의 공지사항 조회 성공";

    public static final String GET_MY_CLUB = "내가 가입한 클럽 조회 성공";

    public static final String GET_MY_INFO = "내 정보 조회 성공";
    public static final String CANCEL_CLUB_JOIN = "클럽 가입 신청 대기 취소 성공";
    public static final String EMAIL_VERIFIED = "이메일 검증 성공";
    public static final String REQUEST_EMAIL_VERIFICATION = "이메일 인증 메일 발송 성공";
    public static final String LEAVE_CLUB_SUCCESS = "클럽 탈퇴 성공";
    public static final String GET_MY_PRODUCT = "내 물품 조회 성공";

    public static final String SEARCH_CLUB_PRODUCT_SUCCESS = "클럽 물품 조회 성공";

    public static final String CHECK_EXPIRED_RENTAL_WAIT = "10분 초과 렌탈 요청 삭제";
    public static final String UPDATE_PRODUCT_INFO = "물품 정보 수정 성공";
    public static final String DELETE_ITEM = "아이템 제거 성공";
    public static final String ADD_ITEM = "아이템 추가 성공";

    public static final String GRANT_ADMIN = "ADMIN 권한 부여";

    public static final String GRANT_USER = "USER 권한 부여";

    public static final String REMOVE_CLUB_MEMBER = "클럽 멤버 삭제";

    public static final String SEARCH_CLUB_RENTALS = "클럽 렌탈 조회";
    public static final String QUIT_SUCCESS = "탈퇴 성공";

    public static final String REPORT_MEMBER = "멤버 신고가 정상적으로 접수되었습니다.";

    public static final String REPORT_NOTIFICATION = "게시글 신고가 정상적으로 접수되었습니다.";
    public static final String SEARCH_CLUB_RETURN = "렌탈 반납 조회 성공";
}
