package com.example.comment.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class UserResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class GetUser {
        private final String userId;
        private final String email;
        private final String password;
    }
}
