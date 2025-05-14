package com.example.comment.domain.auth.repository;

import com.example.comment.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByUserId(String userId);

    RefreshToken findByUserId(String userId);
}
