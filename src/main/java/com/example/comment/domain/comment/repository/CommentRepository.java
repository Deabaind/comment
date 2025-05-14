package com.example.comment.domain.comment.repository;

import com.example.comment.domain.comment.entity.Comment;
import com.example.comment.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByScheduleId(Schedule scheduleId);

    int countByCommentId(Long commentId);

    Page<Comment> findAllByDepth(int depth, Pageable pageable);

    Page<Comment> findAllByDepthAndScheduleId(int depth, Schedule scheduleId, Pageable pageable);

    List<Comment> findAllByCommentId(Long commentId);
}
