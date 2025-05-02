package com.example.comment.domain.nestedComment.entity;

import com.example.comment.domain.comment.entity.Comment;
import com.example.comment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLDelete(sql = "UPDATE comment.nested_comment SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor
public class NestedComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "comment_id")
    private Comment comment;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false, length = 200)
    private String content;
}
