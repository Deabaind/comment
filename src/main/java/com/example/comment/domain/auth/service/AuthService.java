package com.example.comment.domain.auth.service;

import com.example.comment.Exception.ApiException;
import com.example.comment.Exception.ErrorType;
import com.example.comment.domain.auth.dto.AuthRequestDto;
import com.example.comment.domain.auth.dto.AuthResponseDto;
import com.example.comment.domain.user.dto.UserRequestDto;
import com.example.comment.domain.user.entity.User;
import com.example.comment.domain.user.service.UserService;
import com.example.comment.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(AuthRequestDto.SignupRequest signupDto) {
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        UserRequestDto.Signup signupRequest = new UserRequestDto.Signup(signupDto.getEmail(), encodedPassword);
        userService.add(signupRequest);
    }

    // 로그인
    public AuthResponseDto.LoginToken login(AuthRequestDto.LoginRequest loginRequest) {
        // 인증 객체 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        // 토큰 생성
        String accessToken = jwtService.createAccessToken(authentication,
                userId.toString());
        String refreshToken = jwtService.createRefreshToken(authentication,
                userId.toString());
        return new AuthResponseDto.LoginToken(accessToken, "Bearer " + refreshToken);
    }

    // 로그아웃
    public void logout(Long userId) {
        jwtService.deleteRefreshToken(userId);
    }
}
