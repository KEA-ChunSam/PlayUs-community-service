package com.playus.communityservice.domain.comment.document;

import com.playus.communityservice.domain.comment.entity.CommentGroup;
import com.playus.communityservice.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(value = "comments")
public class CommentDocument extends BaseTimeEntity {

    @Id
    private Long id;

    @NotNull
    @Field(name = "user_id")
    private Long userId;

    @NotNull
    @DBRef(lazy = true)
    @Field(name = "comment_group_id")
    private CommentGroup commentGroup;

    @NotNull
    @Field(name = "comment_order")
    private Long commentOrder;

    @NotNull
    @Lob
    private String content;

    @NotNull
    private boolean activated;

    @Builder
    private CommentDocument(Long id, Long userId, CommentGroup commentGroup, Long commentOrder, String content, boolean activated) {
        this.id = id;
        this.userId = userId;
        this.commentGroup = commentGroup;
        this.commentOrder = commentOrder;
        this.content = content;
        this.activated = activated;
    }

    public static CommentDocument createForOnlyTest(Long id, Long userId, CommentGroup commentGroup, Long commentOrder, String content) {
        return CommentDocument.builder()
                .id(id)
                .userId(userId)
                .commentGroup(commentGroup)
                .commentOrder(commentOrder)
                .content(content)
                .activated(true)
                .build();
    }
}
