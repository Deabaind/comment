package com.example.comment.domain.comment.controller;

import com.example.comment.domain.comment.dto.CommentRequestDto;
import com.example.comment.domain.comment.dto.CommentResponseDto;
import com.example.comment.domain.comment.service.CommentService;
import com.example.comment.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 또는 대댓글 생성
    @PostMapping("/{scheduleId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long scheduleId, @Valid @RequestBody CommentRequestDto.Add addDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.addComment(customUserDetails.getUserId(), scheduleId, addDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 단일 댓글과 댓글의 대댓글 조회
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto.GetOne> getOneComment(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getOneComment(commentId));
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequestDto.Update updateDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.updateComment(customUserDetails.getUserId(), commentId, updateDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        commentService.deleteComment(customUserDetails.getUserId(), commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
