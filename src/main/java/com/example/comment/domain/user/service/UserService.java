package com.example.comment.domain.user.service;

import com.example.comment.Exception.ApiException;
import com.example.comment.Exception.ErrorType;
import com.example.comment.domain.user.dto.UserRequestDto;
import com.example.comment.domain.user.entity.User;
import com.example.comment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 생성
    @Transactional
    public void add(UserRequestDto.Signup signupDto) {
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "계정이 존재하는 이메일입니다.");
        }
        userRepository.save(new User(signupDto.getEmail(), signupDto.getPassword()));
    }

    // email로 유저 조회
    @Transactional
    public User getUserByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.INVALID_PARAMETER, "존재하지 않는 이메일 또는 잘못된 비밀번호입니다.");
        }
        return userRepository.findByEmail(email);
    }

    // 유저 조회
    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, ErrorType.NO_RESOURCE, "존재하지 않는 유저입니다."));
    }
}
