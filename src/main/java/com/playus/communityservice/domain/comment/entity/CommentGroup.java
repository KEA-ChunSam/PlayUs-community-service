package com.playus.communityservice.domain.comment.entity;

import com.playus.communityservice.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_group")
public class CommentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    private CommentGroup(Post post) {
        this.post = post;
    }

    public static CommentGroup create(Post post) {
        return CommentGroup.builder()
                .post(post)
                .build();
    }
}
