package com.playus.communityservice.domain.comment.document;

import com.playus.communityservice.domain.common.BaseTimeEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
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
    @Field(name = "comment_group_id")
    private Long commentGroupId;

    @NotNull
    @Field(name = "comment_order")
    private Long commentOrder;

    @NotNull
    private String content;

    @NotNull
    private boolean activated;

    @Builder
    private CommentDocument(Long id, Long userId, Long commentGroupId, Long commentOrder, String content, boolean activated) {
        this.id = id;
        this.userId = userId;
        this.commentGroupId = commentGroupId;
        this.commentOrder = commentOrder;
        this.content = content;
        this.activated = activated;
    }

    public static CommentDocument createForOnlyTest(Long id, Long userId, Long commentGroupId, Long commentOrder, String content) {
        return CommentDocument.builder()
                .id(id)
                .userId(userId)
                .commentGroupId(commentGroupId)
                .commentOrder(commentOrder)
                .content(content)
                .activated(true)
                .build();
    }
}
