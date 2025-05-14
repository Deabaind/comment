package com.example.comment.domain.auth.controller;

import com.example.comment.domain.auth.dto.AuthRequestDto;
import com.example.comment.domain.auth.dto.AuthResponseDto;
import com.example.comment.domain.auth.service.AuthService;
import com.example.comment.domain.user.entity.User;
import com.example.comment.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody AuthRequestDto.SignupRequest signupDto) {
        authService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto.LoginToken> login(@Valid @RequestBody AuthRequestDto.LoginRequest loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        authService.logout(customUserDetails.getUserId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
