package com.example.comment.domain.schedule.service;

import com.example.comment.Exception.ApiException;
import com.example.comment.Exception.ErrorType;
import com.example.comment.domain.comment.dto.CommentResponseDto;
import com.example.comment.domain.comment.service.CommentService;
import com.example.comment.domain.schedule.dto.ScheduleRequestDto;
import com.example.comment.domain.schedule.dto.ScheduleResponseDto;
import com.example.comment.domain.schedule.entity.Schedule;
import com.example.comment.domain.schedule.repository.ScheduleRepository;
import com.example.comment.domain.user.entity.User;
import com.example.comment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentService commentService;
    private final UserService userService;

    // 일정 생성
    @Transactional
    public void addSchedule(Long userId, ScheduleRequestDto.Add addDto) {
        User user = userService.getUser(userId);
        Schedule schedule = new Schedule(user, addDto.getTitle(), addDto.getContent());
        scheduleRepository.save(schedule);
    }

    // 전체 일정 및 댓글 개수 조회
    @Transactional
    public Page<ScheduleResponseDto.GetAll> getAllSchedule(Pageable pageable) {
        Page<Schedule> schedulesPages = scheduleRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")));
        Page<ScheduleResponseDto.GetAll> schedulePageResponseDtos = schedulesPages.map(
                s -> new ScheduleResponseDto.GetAll(s.getUser().getEmail(), s.getTitle(), s.getCreatedAt(), commentService.countComment(s.getId())));
        return schedulePageResponseDtos;
    }

    // 단일 일정 및 댓글 조회
    @Transactional
    public ScheduleResponseDto.GetOne getOneSchedule(Long scheduleId, Pageable pageable) {
        Schedule schedule = getSchedule(scheduleId);
        Page<CommentResponseDto.GetComment> commentPage = commentService.getAllComment(schedule, pageable);
        return new ScheduleResponseDto.GetOne(
                schedule.getUser().getEmail(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt(),
                commentPage);
    }

    // 일정 수정
    @Transactional
    public void updateSchedule(Long userId, Long scheduleId, ScheduleRequestDto.Update updateDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "일정이 존재하지 않습니다."));

        if (schedule.getUser().getId() != userId) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER, "자신의 일정만 수정할 수 있습니다.");
        }
        if (updateDto.getTitle() == null && updateDto.getContent() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorType.INVALID_PARAMETER, "제목 또는 댓글을 입력해주세요.");
        }
        // todo 메서드로 가져온 entity 객체가 트렌젝션으로 수정이 되는가 확인 필요
        getSchedule(scheduleId).update(updateDto);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        // todo 메서드로 가져온 entity 객체가 트렌젝션으로 삭제(수정)가 되는가 확인 필요
        Schedule schedule = getSchedule(scheduleId);
        if (schedule.getUser().getId() != userId) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER, "자신의 일정만 삭제할 수 있습니다.");
        }
        scheduleRepository.delete(getSchedule(scheduleId));
    }

    // 일정을 조회하여 없으면 예외 처리하는 메서드
    public Schedule getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "일정을 찾을 수 없습니다."));
        return schedule;
    }
}
