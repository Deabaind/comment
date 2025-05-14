package com.example.comment.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AuthRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class SignupRequest {

        @NotBlank(message = "이메일는 필수 입력 항목입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private final String email;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{8,20}$", message = "비밀번호는 8~20자 사이의 영문자와 숫자만 사용하며, 대문자와 숫자를 각각 1개 이상 포함해야 합니다.")
        private final String password;

    }

    @Getter
    @RequiredArgsConstructor
    public static class LoginRequest {

        @NotBlank(message = "이메일는 필수 입력 항목입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private final String email;

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 8, max = 20, message = "8자 이상 20자 이하로 입력해주세요.")
        private final String password;
    }

}
