package com.example.comment.domain.user.service;

import com.example.comment.Exception.ApiException;
import com.example.comment.Exception.ErrorType;
import com.example.comment.domain.user.entity.User;
import com.example.comment.domain.user.repository.UserRepository;
import com.example.comment.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!userRepository.existsByEmail(email)) {
            throw new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "존재하지 않는 이메일 또는 잘못된 비밀번호입니다.");
        }
        User user = userRepository.findByEmail(email);
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword());
    }
}
