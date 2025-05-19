package com.playus.communityservice.domain.comment.dto.comment_delete;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CommentDeleteRequest(
    @NotNull(message = "commentId는 null일 수 없습니다!")
    Long commentId
) {
    public static CommentDeleteRequest of(Long commentId) {
        return CommentDeleteRequest.builder()
                .commentId(commentId)
                .build();
    }
}
