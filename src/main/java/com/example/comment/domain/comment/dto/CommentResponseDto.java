package com.example.comment.domain.comment.dto;

import com.example.comment.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class GetOne {
        private final String scheduleTitle;

        private final String userEmail;

        private final String content;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private final LocalDateTime createAt;

        private final List<CommentResponseDto.Reply> replyList;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Reply {
        private final Long writerId;

        private final String content;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private final LocalDateTime createAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class GetComment {
        private final Long writerId;

        private final String content;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private final LocalDateTime createAt;

        private final int countComment;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Aaa {
        private final String ScheduleTitle;


    }


}
