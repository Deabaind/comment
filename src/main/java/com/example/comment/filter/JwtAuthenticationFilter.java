package com.example.comment.filter;

import com.example.comment.domain.auth.dto.AuthResponseDto;
import com.example.comment.domain.auth.service.JwtService;
import com.example.comment.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtService.resolveToken(request);
        String refreshToken = jwtService.resolveRefreshToken(request);

        // accessToken 이 만료되지 않았고 인증되었다면 인증 정보 저장
        if (StringUtils.hasText(accessToken) && jwtService.validateAccessToken(accessToken)) {
            log.info("accessToken 인증 성공");
            // accessToken 으로 인증를 정보 저장하는 코드
            setAuthentication(accessToken, request);
        }
        // accessToken 이 만료되었을 때 refreshToken 이 인증되었다면 토큰 재발급 및 인증 정보 저장
        else if (StringUtils.hasText(refreshToken) && jwtService.validateToken(refreshToken)) {
            log.info("refreshToken으로 accessToken 재발급 및 인증 성공");
            //  access, refresh Token 재발급
            String newAccessToken = reissueToken(refreshToken, response);

            // 재발급 받은 AccessToken 으로 인증를 정보 저장하는 코드
            setAuthentication(newAccessToken, request);
        }
        filterChain.doFilter(request, response);
    }

    // 인증 정보를 저장하는 메서드
    private void setAuthentication(String token, HttpServletRequest request) {
        CustomUserDetails userDetails = jwtService.getUserDetails(token);

        // 사용자의 인증 객체에 id를 넣고 비밀번호는 보안을 위해 제외, 권한은 없기에 null로 저장
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 요청에 대해 추가 정보를 Authentication 객체에 넣어주어 추후에 로그나 추적할 때 사용
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 인증 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 토큰 재발급
    private String reissueToken(String refreshToken, HttpServletResponse response) {

        CustomUserDetails userDetailsDto = jwtService.getUserDetails(refreshToken);

        String newAccessToken = jwtService.createAccessToken(userDetailsDto.getUsername(), userDetailsDto.getEmail());
        String newRefreshToken = jwtService.createRefreshToken(userDetailsDto.getUsername(), userDetailsDto.getEmail());

        response.setHeader("Authorization", newAccessToken);
        response.setHeader("Authorization-refresh", "Bearer " + newRefreshToken);

        return newAccessToken;
    }
}
