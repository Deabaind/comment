package com.example.comment.domain.comment.service;

import com.example.comment.Exception.ApiException;
import com.example.comment.Exception.ErrorType;
import com.example.comment.domain.comment.dto.CommentRequestDto;
import com.example.comment.domain.comment.dto.CommentResponseDto;
import com.example.comment.domain.comment.entity.Comment;
import com.example.comment.domain.comment.repository.CommentRepository;
import com.example.comment.domain.schedule.entity.Schedule;
import com.example.comment.domain.schedule.repository.ScheduleRepository;
import com.example.comment.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    // 댓글, 대댓글 생성
    @Transactional
    public void addComment(Long userId, Long scheduleId, CommentRequestDto.Add addDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "일정을 찾을 수 없습니다."));
        Comment comment = new Comment(schedule, userService.getUser(userId), addDto.getContent());
        if (addDto.getCommentId() != null) {
            Comment selectComment = getComment(addDto.getCommentId());
            selectComment.addReplyUpdateComment();
            comment.addReply(selectComment.getDepth(), addDto.getCommentId());
        }
        commentRepository.save(comment);
    }

    // 단일 댓글 및 대댓글 조회
    @Transactional
    public CommentResponseDto.GetOne getOneComment(Long commentId) {
        Comment findComment = getComment(commentId);
        List<Comment> commentList = commentRepository.findAllByCommentId(commentId);
        List<CommentResponseDto.Reply> replyList = new ArrayList<>();
        for (Comment comment : commentList) {
            replyList.add(new CommentResponseDto.Reply(
                    comment.getUserId().getId(),
                    comment.getContent(),
                    comment.getCreatedAt()
            ));
        }
        return new CommentResponseDto.GetOne(
                findComment.getScheduleId().getTitle(),
                findComment.getUserId().getEmail(),
                findComment.getContent(),
                findComment.getCreatedAt(),
                replyList);
    }

    // 댓글 목록 조회
    @Transactional
    public Page<CommentResponseDto.GetComment> getAllComment(Schedule schedule, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAllByDepthAndScheduleId(0, schedule, pageable);
        Page<CommentResponseDto.GetComment> dtoPage = commentPage.map(
                d -> new CommentResponseDto.GetComment(d.getUserId().getId(), d.getContent(), d.getCreatedAt(), countReply(d.getId()))
        );
        return dtoPage;
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequestDto.Update updateDto) {
        Comment comment = getComment(commentId);
        if (comment.getUserId().getId() != userId) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER, "자신의 댓글만 수정할 수 있습니다.");
        }
        comment.update(updateDto.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        if (comment.getUserId().getId() != userId) {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorType.INVALID_PARAMETER, "자신의 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(getComment(commentId));
    }

    // 댓글을 조회하여 없으면 예외 처리하는 메서드
    public Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "댓글을 찾을 수 없습니다."));
        return comment;
    }

    // 일정의 댓글 개수 조회
    public int countComment(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.NO_RESOURCE, "일정을 찾을 수 없습니다."));
        return commentRepository.countByScheduleId(schedule);
    }

    // 댓글의 대댓글 개수 조회
    public int countReply(Long commentId) {
        return commentRepository.countByCommentId(commentId);
    }
}
