package com.travel.exception;

import lombok.Getter;

@Getter
public enum ApiExceptionType implements BaseExceptionType {
    // 로그인 관련 Type
    NO_LOGIN("NO_LOGIN", 401, "로그인 필요"),
    NO_ADMIN("NO_ADMIN", 403, "권한 없는 사용자"),
    EXIST_USER("EXIST_USER", 200, "동일한 ID 유저 존재"),

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

    NOT_FOUND_COMMON("NOT_FOUND_COMMON", 404, "해당 공통코드 없음"),
    NOT_FOUND_COMMON_LIST("NOT_FOUND_COMMON_LIST", 404, "공통 코드 리스트 없음"),

    // 여행 코드 관련 TYPE
    NOT_FOUND_TRAVEL_REVIEW_LIST("NOT_FOUND_TRAVEL_REVIEW_LIST", 404, "여행 댓글 리스트 없음"),
    NOT_FOUND_TRAVEL_REVIEW("NOT_FOUND_TRAVEL_REVIEW", 404, "여행 댓글 상세 없음"),
    ERROR_REVIEW_TRAVEL("ERROR_REVIEW_TRAVEL", 500, "여행 댓글 등록 에러"),
    ERROR_UPDATE_REVIEW_TRAVEL("ERROR_UPDATE_REVIEW_TRAVEL", 500, "여행 댓글 수정 에러"),
    ERROR_DELETE_REVIEW_TRAVEL("ERROR_DELETE_REVIEW_TRAVEL", 500, "여행 댓글 삭제 에러"),
    ERROR_FAVORITE_TRAVEL("ERROR_FAVORITE_TRAVEL", 500, "여행 좋아요 에러"),
    NOT_FOUND_TRAVEL("NOT_FOUND_TRAVEL", 404, "여행 상세 없음"),
    NOT_FOUND_TRAVEL_LIST("NOT_FOUND_TRAVEL_LIST", 404, "여행 소개 리스트 없음"),
    NOT_FOUND_TRAVEL_GROUP_LIST("NOT_FOUND_TRAVEL_GROUP_LIST", 404, "여행 그룹 리스트 없음"),
    NOT_FOUND_TRAVEL_GROUP("NOT_FOUND_TRAVEL_GROUP", 404, "여행 그룹 상세 없음"),
    NOT_FOUND_SCHEDULE_LIST("NOT_FOUND_SCHEDULE_LIST", 404, "스케줄 리스트 없음"),
    NOT_FOUND_SCHEDULE("NOT_FOUND_SCHEDULE", 200, "스케줄 상세 없음"),
    ERROR_TRAVEL_SCHEDULE("ERROR_TRAVEL_SCHEDULE", 500, "유저 여행 스케줄 등록 에러"),
    ERROR_UPDATE_TRAVEL_SCHEDULE("ERROR_UPDATE_TRAVEL_SCHEDULE", 500, "유저 여행 스케줄 수정 에러"),
    ERROR_DELETE_TRAVEL_SCHEDULE("ERROR_DELETE_TRAVEL_SCHEDULE", 500, "유저 여행 스케줄 삭제 에러"),
    NOT_FOUND_TRAVEL_RECOMMEND_LIST("NOT_FOUND_TRAVEL_RECOMMEND_LIST", 404, "여행지 추천검색어 리스트 없음"),
    NOT_FOUND_TRAVEL_RECOMMEND("NOT_FOUND_TRAVEL_RECOMMEND", 404, "여행지 추천검색어 상세 없음"),
    NOT_FOUND_FESTIVAL_LIST("NOT_FOUND_FESTIVAL_LIST", 404, "축제 리스트 없음"),
    NOT_FOUND_FESTIVAL("NOT_FOUND_FESTIVAL", 404, "축제 상세 없음"),

    // 공지사항 관련
    NOT_FOUND_NOTICE_LIST("NOT_FOUND_NOTICE_LIST", 404, "공지사항 리스트 없음"),
    NOT_FOUND_NOTICE("NOT_FOUND_NOTICE", 404, "공지사항 상세 없음"),

    // 게시글 관련
    NOT_FOUND_POST_LIST("NOT_FOUND_POST_LIST", 404, "게시글 리스트 없음"),
    NOT_FOUND_POST("NOT_FOUND_POST", 404, "게시글 상세 없음"),
    ERROR_POST("ERROR_POST", 500, "게시글 등록 에러"),
    ERROR_UPDATE_POST("ERROR_UPDATE_POST", 500, "게시글 수정 에러"),
    ERROR_DELETE_POST("ERROR_DELETE_POST", 500, "게시글 삭제 에러"),
    NOT_FOUND_REPLY("NOT_FOUND_REPLY", 404, "댓글 상세 없음"),
    ERROR_REPLY("ERROR_REPLY", 500, "댓글 등록 에러"),
    ERROR_UPDATE_REPLY("ERROR_UPDATE_REPLY", 500, "댓글 수정 에러"),
    ERROR_DELETE_REPLY("ERROR_DELETE_REPLY", 500, "댓글 삭제 에러"),

    // FAQ 관련
    NOT_FOUND_FAQ_LIST("NOT_FOUND_FAQ_LIST", 404, "FAQ 리스트 없음"),
    NOT_FOUND_FAQ("NOT_FOUND_FAQ", 404, "FAQ 상세 없음");


    private final String errorCode;
    private final int httpStatus;
    private final String errorMessage;

    ApiExceptionType(String errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
