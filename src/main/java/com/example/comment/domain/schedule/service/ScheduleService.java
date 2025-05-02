package com.example.comment.domain.schedule.service;

import com.example.comment.domain.schedule.dto.ScheduleRequestDto;
import com.example.comment.domain.schedule.entity.Schedule;
import com.example.comment.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 일정 생성
    @Transactional
    public void addSchedule(ScheduleRequestDto.Add add){
        Schedule schedule = new Schedule(add.getWriterId(), add.getTitle(), add.getContent());
        scheduleRepository.save(schedule);
    }
}
