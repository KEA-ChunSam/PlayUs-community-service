package com.playus.communityservice.domain.comment.entity;

import com.playus.communityservice.domain.comment.entity.Comment;
import com.playus.communityservice.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_group_id", nullable = false)
    private CommentGroup commentGroup;

    @Column(name = "comment_order", nullable = false)
    private Long order;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean activated;

    @Builder
    private Comment(Long userId, CommentGroup commentGroup, Long order, String content, boolean activated) {
        this.userId = userId;
        this.commentGroup = commentGroup;
        this.order = order;
        this.content = content;
        this.activated = activated;
    }

    public static Comment create(Long userId, CommentGroup commentGroup, Long order, String content) {
        return Comment.builder()
                .userId(userId)
                .commentGroup(commentGroup)
                .order(order)
                .content(content)
                .activated(true)
                .build();
    }

    public void updateOrder(Long order) {
        this.order = order;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete(boolean activated) {
        this.activated = activated;
    }
}
