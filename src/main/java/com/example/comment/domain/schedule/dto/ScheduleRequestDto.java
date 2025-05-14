package com.example.comment.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ScheduleRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class Add{

        @NotBlank
        @Size(min = 1, max = 30, message = "제목은 30글자까지 입력할 수 있습니다.")
        private final String title;

        @NotBlank
        @Size(min = 1, max = 200, message = "댓글은 200글자까지 입력할 수 있습니다.")
        public final String content;
    }

    @Getter
    public static class Update {

        @Size(min = 1, max = 30, message = "제목은 30글자까지 입력할 수 있습니다.")
        private final String title;

        @Size(min = 1, max = 200, message = "댓글은 200글자까지 입력할 수 있습니다.")
        private final String content;

        public Update(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
