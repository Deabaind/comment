package com.example.comment.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CommentRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class Add {

        private final Long commentId;

        @NotBlank(message = "댓글을 입력해주세요.")
        @Size(max = 200, message = "200글짜까지 입력할 수 있습니다.")
        private final String content;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Update {

        @NotBlank(message = "댓글을 입력해주세요.")
        @Size(max = 200, message = "200글짜까지 입력할 수 있습니다.")
        private final String content;
    }
}
