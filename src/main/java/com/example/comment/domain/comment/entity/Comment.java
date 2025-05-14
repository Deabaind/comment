package com.example.comment.domain.comment.entity;

import com.example.comment.domain.common.entity.BaseEntity;
import com.example.comment.domain.schedule.entity.Schedule;
import com.example.comment.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLDelete(sql = "UPDATE comment.comment SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "schedule_id")
    private Schedule scheduleId;

    private int depth;

    private int countReply;

    private Long commentId;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User userId;

    @Column(nullable = false, length = 200)
    private String content;

    public Comment(Schedule scheduleId, User userId, String content) {
        this.scheduleId = scheduleId;
        this.depth = 0;
        this.countReply = 0;
        this.userId = userId;
        this.content = content;
    }

    public void addReply(int selectCommentDepth, Long commentId) {
        this.depth = selectCommentDepth + 1;
        this.commentId = commentId;
    }

    public void addReplyUpdateComment() {
        this.countReply += 1;
    }

    public void update(String content) {
        this.content = content;
    }
}
