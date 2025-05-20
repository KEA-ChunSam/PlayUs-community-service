package com.playus.communityservice.domain.comment.dto.comment_delete;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CommentDeleteRequest(
    @NotNull(message = "commentId는 null일 수 없습니다!")
    Long commentId,

    @NotNull(message =  "commentGroupID는 null일 수 없습니다.")
    Long commentGroupId
) {
    public static CommentDeleteRequest of(Long commentId, Long commentGroupId) {
        return CommentDeleteRequest.builder()
                .commentId(commentId)
                .commentGroupId(commentGroupId)
                .build();
    }
}
