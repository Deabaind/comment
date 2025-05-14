package com.example.comment.domain.schedule.dto;

import com.example.comment.domain.comment.dto.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class ScheduleResponseDto {

    @Getter
    public static class GetAll {
        private final String writer;

        private final String title;

        private final int commentCount;

        @JsonFormat(pattern = "yy-MM-dd HH:mm")
        private final LocalDateTime createAt;

        public GetAll(String writer, String title, LocalDateTime createAt, int commentCount) {
            this.writer = writer;
            this.title = title;
            this.createAt = createAt;
            this.commentCount = commentCount;
        }
    }

    @Getter
    public static class GetOne {
        private final String writer;

        private final String title;

        private final String content;

        @JsonFormat(pattern = "yy-MM-dd HH:mm")
        private final LocalDateTime createdAt;

        @JsonFormat(pattern = "yy-MM-dd HH:mm")
        private final LocalDateTime updatedAt;

        private final Page<CommentResponseDto.GetComment> commentPage;

        public GetOne(String writer, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Page<CommentResponseDto.GetComment> commentPage) {
            this.writer = writer;
            this.title = title;
            this.content = content;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.commentPage = commentPage;
        }
    }
}
