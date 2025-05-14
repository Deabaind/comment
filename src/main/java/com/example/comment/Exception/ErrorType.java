package com.example.comment.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    NO_RESOURCE("존재하지 않는 데이터입니다."),
    INVALID_PARAMETER("잘못된 파라미터 요청입니다."),
    UNKNOWN_ERROR("알 수 없는 에러입니다."),
    NOT_LOGGED_IN("로그인이 필요한 사용자입니다.");

    private final String description;
}
