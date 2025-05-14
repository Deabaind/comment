package com.example.comment.domain.schedule.controller;

import com.example.comment.domain.auth.dto.AuthResponseDto;
import com.example.comment.domain.schedule.dto.ScheduleRequestDto;
import com.example.comment.domain.schedule.dto.ScheduleResponseDto;
import com.example.comment.domain.schedule.service.ScheduleService;
import com.example.comment.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping
    public ResponseEntity<Void> addSchedule(@Valid @RequestBody ScheduleRequestDto.Add requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        scheduleService.addSchedule(customUserDetails.getUserId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 전체 일정 조회
    @GetMapping
    public ResponseEntity<Page<ScheduleResponseDto.GetAll>> getAllSchedule(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getAllSchedule(pageable));
    }

    // 단일 일정 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto.GetOne> getOneSchedule(@PathVariable Long scheduleId, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getOneSchedule(scheduleId, pageable));
    }

    // 일정 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto.Update updateDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        scheduleService.updateSchedule(customUserDetails.getUserId(), scheduleId, updateDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        scheduleService.deleteSchedule(customUserDetails.getUserId(), scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
