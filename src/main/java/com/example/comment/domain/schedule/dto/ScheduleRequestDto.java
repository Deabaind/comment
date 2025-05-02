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

        @NotNull
        private final Long writerId;

        @NotBlank
        @Size(min = 1, max = 30)
        private final String title;

        @NotBlank
        @Size(min = 1, max = 200)
        public final String content;
    }
}
