package com.travel.travel_project.exception;

import lombok.Getter;

@Getter
public enum ApiExceptionType implements BaseExceptionType {
    // 로그인 관련 Type
    NO_LOGIN("NO_LOGIN", 401, "로그인 필요"),
    NO_ADMIN("NO_ADMIN", 403, "권한 없는 사용자"),

    // User 관련 Type
    ERROR_USER("ERROR_USER", 500, "유저 등록 에러"),
    ERROR_UPDATE_USER("ERROR_UPDATE_USER", 500, "유저 수정 에러"),
    ERROR_DELETE_USER("ERROR_DELETE_USER", 500, "유저 삭제 에러"),
    NOT_FOUND_USER("NOT_FOUND_USER", 200, "해당 유저 없음"),
    NOT_FOUND_USER_LIST("NOT_FOUND_USER_LIST", 200, "유저 리스트 없음"),

    // 서버 관련 TYPE
    RUNTIME_EXCEPTION("SERVER_ERROR", 500, "서버에러"),
    BAD_REQUEST("", 401, "권한에러"),
    NOT_NULL("NOT_NULL", 400, "필수값 누락"),
    ID_EXIST("ID_EXIST", 400, "같은 아이디 존재"),

    // 이미지 관련 TYPE
    ERROR_IMAGE("ERROR_IMAGE", 500, "이미지 등록 에러"),
    ERROR_UPDATE_IMAGE("ERROR_UPDATE_IMAGE", 500, "이미지 수정 에러"),
    ERROR_DELETE_IMAGE("ERROR_DELETE_IMAGE", 500, "이미지 삭제 에러"),

    // 공통 코드 관련 TYPE
    ERROR_COMMON("ERROR_COMMON", 500, "공통 코드 등록 에러"),
    ERROR_UPDATE_COMMON("ERROR_UPDATE_COMMON", 500, "공통 코드 수정 에러"),
    ERROR_DELETE_COMMON("ERROR_DELETE_COMMON", 500, "공통 코드 삭제 에러"),
    NOT_FOUND_COMMON("NOT_FOUND_COMMON", 200, "해당 공통코드 없음"),
    NOT_FOUND_COMMON_LIST("NOT_FOUND_COMMON_LIST", 200, "공통 코드 리스트 없음"),

    // 여행 코드 관련 TYPE
    ERROR_TRAVEL("ERROR_TRAVEL", 500, "여행 등록 에러"),
    NOT_FOUND_TRAVEL_REVIEW_LIST("NOT_FOUND_TRAVEL_REVIEW_LIST", 200, "여행 댓글 리스트 없음"),
    NOT_FOUND_TRAVEL_REVIEW("NOT_FOUND_TRAVEL_REVIEW", 200, "여행 댓글 상세 없음"),
    ERROR_REVIEW_TRAVEL("ERROR_REVIEW_TRAVEL", 500, "여행 댓글 등록 에러"),
    ERROR_UPDATE_REVIEW_TRAVEL("ERROR_UPDATE_REVIEW_TRAVEL", 500, "여행 댓글 수정 에러"),
    ERROR_DELETE_REVIEW_TRAVEL("ERROR_DELETE_REVIEW_TRAVEL", 500, "여행 댓글 삭제 에러"),
    ERROR_UPDATE_TRAVEL("ERROR_UPDATE_TRAVEL", 500, "여행 수정 에러"),
    ERROR_DELETE_TRAVEL("ERROR_DELETE_TRAVEL", 500, "여행 삭제 에러"),
    ERROR_FAVORITE_TRAVEL("ERROR_FAVORITE_TRAVEL", 500, "여행 좋아요 에러"),
    NOT_FOUND_TRAVEL("NOT_FOUND_TRAVEL", 200, "여행 상세 없음"),
    NOT_FOUND_TRAVEL_LIST("NOT_FOUND_TRAVEL_LIST", 200, "여행 소개 리스트 없음"),
    NOT_FOUND_TRAVEL_GROUP_LIST("NOT_FOUND_TRAVEL_GROUP_LIST", 200, "여행 그룹 리스트 없음"),
    NOT_FOUND_TRAVEL_GROUP("NOT_FOUND_TRAVEL_GROUP", 200, "여행 그룹 상세 없음"),
    ERROR_TRAVEL_GROUP("ERROR_TRAVEL_GROUP", 500, "여행 그룹 등록 에러"),
    ERROR_UPDATE_TRAVEL_GROUP("ERROR_UPDATE_TRAVEL_GROUP", 500, "여행 그룹 수정 에러"),
    ERROR_DELETE_TRAVEL_GROUP("ERROR_DELETE_TRAVEL_GROUP", 500, "여행 그룹 삭제 에러"),
    ERROR_TRAVEL_GROUP_UESR("ERROR_TRAVEL_GROUP_USER", 500, "유저 여행 그룹 등록 에러"),
    ERROR_DELETE_TRAVEL_GROUP_USER("ERROR_DELETE_TRAVEL_GROUP_USER", 500, "유저 여행 그룹 삭제 에러"),
    ERROR_TRAVEL_SCHEDULE("ERROR_TRAVEL_SCHEDULE", 500, "유저 여행 스케줄 등록 에러"),
    ERROR_UPDATE_TRAVEL_SCHEDULE("ERROR_UPDATE_TRAVEL_SCHEDULE", 500, "유저 여행 스케줄 수정 에러"),
    ERROR_DELETE_TRAVEL_SCHEDULE("ERROR_DELETE_TRAVEL_SCHEDULE", 500, "유저 여행 스케줄 삭제 에러"),

    // 공지사항 관련
    NOT_FOUND_NOTICE_LIST("NOT_FOUND_NOTICE_LIST", 200, "공지사항 리스트 없음"),
    NOT_FOUND_NOTICE("NOT_FOUND_NOTICE", 200, "공지사항 상세 없음"),
    ERROR_NOTICE("ERROR_NOTICE", 500, "공지사항 등록 에러"),
    ERROR_UPDATE_NOTICE("ERROR_UPDATE_NOTICE", 500, "공지사항 수정 에러"),
    ERROR_DELETE_NOTICE("ERROR_DELETE_NOTICE", 500, "공지사항 삭제 에러"),

    // 게시글 관련
    NOT_FOUND_POST_LIST("NOT_FOUND_POST_LIST", 200, "게시글 리스트 없음"),
    NOT_FOUND_POST("NOT_FOUND_POST", 200, "게시글 상세 없음"),
    ERROR_POST("ERROR_POST", 500, "게시글 등록 에러"),
    ERROR_UPDATE_POST("ERROR_UPDATE_POST", 500, "게시글 수정 에러"),
    ERROR_DELETE_POST("ERROR_DELETE_POST", 500, "게시글 삭제 에러"),

    // FAQ 관련
    NOT_FOUND_FAQ_LIST("NOT_FOUND_FAQ_LIST", 200, "FAQ 리스트 없음"),
    NOT_FOUND_FAQ("NOT_FOUND_FAQ", 200, "FAQ 상세 없음"),
    ERROR_FAQ("ERROR_FAQ", 500, "FAQ 등록 에러"),
    ERROR_UPDATE_FAQ("ERROR_UPDATE_FAQ", 500, "FAQ 수정 에러"),
    ERROR_DELETE_FAQ("ERROR_DELETE_FAQ", 500, "FAQ 삭제 에러");


    private final String errorCode;
    private final int httpStatus;
    private final String errorMessage;

    ApiExceptionType(String errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
