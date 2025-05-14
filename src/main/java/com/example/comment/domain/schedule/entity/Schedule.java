package com.example.comment.domain.schedule.entity;

import com.example.comment.domain.common.entity.BaseEntity;
import com.example.comment.domain.schedule.dto.ScheduleRequestDto;
import com.example.comment.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLDelete(sql = "UPDATE comment.schedule SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User userId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    public Schedule(User userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public void update(ScheduleRequestDto.Update updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }
}
