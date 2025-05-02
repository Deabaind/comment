package com.example.comment.domain.comment.entity;

import com.example.comment.domain.common.BaseEntity;
import com.example.comment.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLDelete(sql = "UPDATE comment.comment SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "schedule_id")
    private Schedule schedule;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false, length = 200)
    private String content;
}
