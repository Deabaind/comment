package com.example.comment.domain.schedule.controller;

import com.example.comment.domain.schedule.dto.ScheduleRequestDto;
import com.example.comment.domain.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;

    // 일정 생성
    public ResponseEntity<Void> addSchedule(@Valid @RequestBody ScheduleRequestDto.Add requestDto){
        scheduleService.addSchedule(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
