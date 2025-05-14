package com.example.comment.domain.auth.service;

import com.example.comment.Exception.ApiException;
import com.example.comment.Exception.ErrorType;
import com.example.comment.domain.auth.entity.RefreshToken;
import com.example.comment.domain.auth.repository.RefreshTokenRepository;
import com.example.comment.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Getter
@Service
@Slf4j
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Value("${jwt.secret}")
    private String secretKeyString;

    private Key secretKey;

    // AccessToken
    private final long accessTokenValidityMilliseconds = 6000000;
    // reFreshToken
    private final long refreshTokenValidityInMilliseconds = 600000;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // 로그인시 AccessToken 생성
    public String createAccessToken(Authentication authentication, String userId) {
        String email = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidityMilliseconds);

        return Jwts.builder()
                .setSubject(userId)
                .claim("tokenType", "access")
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // AccessToken 재발급
    public String createAccessToken(String userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidityMilliseconds);

        return Jwts.builder()
                .setSubject(userId)
                .claim("tokenType", "access")
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 로그인시 RefreshToken 생성
    @Transactional
    public String createRefreshToken(Authentication authentication, String userId) {
        String email = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .claim("tokenType", "refresh")
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        RefreshToken saveToken = refreshTokenRepository.findByUserId(userId);
        if (saveToken == null) {
            saveToken = RefreshToken.builder()
                    .userId(userId)
                    .refreshToken(refreshToken)
                    .expiryDate(expiryDate)
                    .build();
        } else {
            saveToken.update(userId, refreshToken, expiryDate);
        }
        refreshTokenRepository.save(saveToken);

        return refreshToken;
    }

    // RefreshToken 재발급
    @Transactional
    public String createRefreshToken(String userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .claim("tokenType", "refresh")
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        // 기존의 토큰 업데이트
        RefreshToken saveToken = refreshTokenRepository.findByUserId(userId);
        saveToken.update(userId, refreshToken, expiryDate);

        return refreshToken;
    }

    // 요청 헤더에서 AccessToken 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 요청 헤더에서 RefreshToken 가져오기
    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization-refresh");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            // todo 토큰 만료 등 각 상황 예외 처리
            return false;
        }
    }

    // accessToken 유효성 검증
    public boolean validateAccessToken(String jwtToken) {
        try {
            Long userId = getUserDetails(jwtToken).getUserId();

            validateToken(jwtToken);

            if (!refreshTokenRepository.existsByUserId(userId.toString())) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.NOT_LOGGED_IN, "로그아웃된 사용자입니다.");
            }
            return true;
        } catch (Exception e) {
            // todo 토큰 만료 등 각 상황 예외 처리
            return false;
        }
    }

    // 토큰에서 claims를 꺼내는 메서드
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 userDetails를 꺼내는 메서드
    public CustomUserDetails getUserDetails(String token) {
        String userId = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Long id = Long.valueOf(userId);
        String email = getClaims(token).get("email", String.class);
        CustomUserDetails customUserDetails = new CustomUserDetails(id, email);
        return customUserDetails;
    }

    // refreshToken 삭제하는 메서드
    public void deleteRefreshToken(Long userId) {
        if (!refreshTokenRepository.existsByUserId(userId.toString())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.NO_RESOURCE, "이미 로그아웃된 사용자입니다.");
        }
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId.toString());
        refreshTokenRepository.deleteById(refreshToken.getId());
    }
}
