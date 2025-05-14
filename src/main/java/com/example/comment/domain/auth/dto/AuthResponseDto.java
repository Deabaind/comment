package com.example.comment.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AuthResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class LoginToken {
        private final String accessToken;
        private final String refreshToken;
    }

    // todo 삭제? 해야하나?
    @Getter
    @RequiredArgsConstructor
    public static class UserDetailsDto {
        private final Long userId;
        private final String email;
    }
}
