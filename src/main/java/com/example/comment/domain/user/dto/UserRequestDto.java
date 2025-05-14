package com.example.comment.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class UserRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class Signup {

        private final String email;

        private final String password;
    }
}
